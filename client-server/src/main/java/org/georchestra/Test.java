package org.georchestra;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Test {

    @Autowired
    private DefaultController controller;

    @PostConstruct
    public void init() throws InterruptedException {

        final Test instance = this;
        new Thread(new Runnable(){
            public void run(){
                try {
                    instance.async();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void async() throws Exception {

        /*
         * Test regular login with cas server and login form
         * * target webapp should receive headers : sec-username, ....
         */
        DesiredCapabilities capability = DesiredCapabilities.firefox();

        RemoteWebDriver driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), capability);
        Wait<WebDriver> wait = new WebDriverWait(driver, 30);

        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().window().setSize(new Dimension(1920, 1080));

        //Should redirect to cas login page
        driver.get("http://proxy:8080/header/?login");

        // Verify that cas login page is sent
        this.checkPath(driver, "/cas/login");

        driver.findElement(By.id("username")).sendKeys("testadmin");
        driver.findElement(By.id("password")).sendKeys("testadmin");
        driver.findElement(By.name("submit")).click();

        // Should contains '/header/'
        this.checkPath(driver, "/header/");

        // Check HTTP headers sent by SP
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        expectedHeaders.put("sec-roles",
                "ROLE_MOD_EXTRACTORAPP;ROLE_USER;ROLE_ADMINISTRATOR;ROLE_GN_ADMIN;ROLE_MOD_LDAPADMIN;ROLE_MOD_ANALYTICS");
        expectedHeaders.put("sec-org", "psc");
        expectedHeaders.put("sec-email", "psc+testadmin@georchestra.org");
        expectedHeaders.put("sec-username", "testadmin");
        expectedHeaders.put("sec-orgname", "Project Steering Committee");
        expectedHeaders.put("sec-proxy","true");
        expectedHeaders.put("sec-lastname", "ADMIN");
        expectedHeaders.put("sec-firstname", "Test");

        for(WebElement el : driver.findElements(By.id("headers")).get(0).findElements(By.tagName("li"))){
            String[] matches = el.getText().split(": ",2);
            if(expectedHeaders.containsKey(matches[0]) && expectedHeaders.get(matches[0]).equals(matches[1]))
                expectedHeaders.remove(matches[0]);
        }
        if(expectedHeaders.size() > 0){
            throw new Exception("Some header cannot be found or has wrong value : " + expectedHeaders.toString());
        }

        driver.quit();
    }

    private void checkPath(RemoteWebDriver driver, String path) throws Exception {
         URL currentUrl = new URL(driver.getCurrentUrl());
        if(currentUrl.getPath() == path){
            throw new Exception("Invalid path, it should be '" + path + "'");
        }
    }

    public void shutdownJetty(int exitCode){
        System.exit(exitCode) ;
    }

}
