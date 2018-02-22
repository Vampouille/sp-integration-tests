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
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Test {

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

        DesiredCapabilities capability = DesiredCapabilities.firefox();

        RemoteWebDriver driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), capability);
        Wait<WebDriver> wait = new WebDriverWait(driver, 30);

        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().window().setSize(new Dimension(1920, 1080));

        //Should redirect to cas login page
        driver.get("http://proxy:8080/header/?login");

        // Verify that cas login page is sent
        URL currentUrl = new URL(driver.getCurrentUrl());
        if(currentUrl.getPath() != "/cas/login"){
            assert false;
        }

        driver.findElement(By.id("username")).sendKeys("testadmin");
        driver.findElement(By.id("password")).sendKeys("testadmin");
        driver.findElement(By.name("submit")).click();

        // Should contains '/header/'
        driver.findElements(By.tagName("body")).get(0).getText();

        WebElement header = driver.findElement(By.id("page-header"));
        if(!(header.isDisplayed())){
            throw new Exception("Not displayed");
        } else {
            System.out.println("Header OK!");
        }

        driver.quit();
    }

    @Autowired
    private DefaultController controller;

    public void test(){
        controller.setResponse("Hello");
    }

    public void shutdownJetty(int exitCode){
        System.exit(exitCode) ;
    }

}
