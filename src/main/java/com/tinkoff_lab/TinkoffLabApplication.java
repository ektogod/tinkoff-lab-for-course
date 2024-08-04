package com.tinkoff_lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TinkoffLabApplication {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(TinkoffLabApplication.class);
        logger.info("Application has started working");

        SpringApplication.run(TinkoffLabApplication.class, args);

        logger.info("Application has terminated working");
    }
}
