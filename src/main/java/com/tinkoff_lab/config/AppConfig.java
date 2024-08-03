package com.tinkoff_lab.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@PropertySource("application.properties")
@Configuration
@Getter
public class AppConfig {
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
