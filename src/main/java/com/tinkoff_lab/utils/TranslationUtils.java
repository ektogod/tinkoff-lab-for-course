package com.tinkoff_lab.utils;

import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.responses.IPResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TranslationUtils {
    private AppConfig config;

    @Autowired
    public TranslationUtils(AppConfig config) {
        this.config = config;
    }

    public String getIP(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<IPResponse> entity = restTemplate.getForEntity(config.getIpUrl(), IPResponse.class);
        return entity
                .getBody()
                .getIp();
    }

    public String getMoscowTime(){
        ZonedDateTime now = ZonedDateTime
                .now()
                .withZoneSameInstant(ZoneId.of("Europe/Moscow"));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return now.format(dtf);
    }
}
