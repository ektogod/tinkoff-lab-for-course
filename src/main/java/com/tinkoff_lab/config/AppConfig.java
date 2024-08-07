package com.tinkoff_lab.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@PropertySource("classpath:application.properties")
@Configuration
@Getter

public class AppConfig {                  // I use this class to have access to application.properties data
    @Value("${translation.url}")
    private String translationURL;

    @Value("${database.url}")
    private String databaseURL;

    @Value("${database.username}")
    private String databaseUsername;

    @Value("${database.password}")
    private String databasePassword;

    @Value("${IP.url}")
    private String ipUrl;
}
