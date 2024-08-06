package com.tinkoff_lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

@SpringBootApplication
public class TinkoffLabApplication {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(TinkoffLabApplication.class);
        logger.info("Application has started working");

        SpringApplication app = new SpringApplication(TinkoffLabApplication.class);
        app.addListeners((ApplicationListener<ContextClosedEvent>) event -> {
            logger.info("Application has terminated working\n");
        });

        app.run(args);
    }
}
