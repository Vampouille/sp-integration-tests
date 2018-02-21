package org.georchestra;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class Test {

    @PostConstruct
    public void init() throws InterruptedException {

        final Test instance = this;

        new Thread(new Runnable(){
            public void run(){
                try {
                    instance.async();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void async() throws InterruptedException {
        int i = 0;
        while(true){
            System.out.println("Sleep");
            Thread.sleep(4000);
            controller.setResponse("" + i);
            i++;
        }
    }

    @Autowired
    private DefaultController controller;

    public void test(){
        controller.setResponse("Hello");
    }
}
