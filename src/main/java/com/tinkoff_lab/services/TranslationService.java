package com.tinkoff_lab.services;

import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.exceptions.ExecutorServiceException;
import com.tinkoff_lab.requests.UserRequest;
import com.tinkoff_lab.dao.TranslationDAO;
import com.tinkoff_lab.responses.TranslateResponse;
import com.tinkoff_lab.responses.UserResponse;
import com.tinkoff_lab.utils.TranslationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class TranslationService {
    private static final int THREADS_CONST = 10;
    private String[] translatedWords;

    private AppConfig appConfig;
    private TranslationDAO dao;
    private TranslationUtils utils;

    @Autowired
    public TranslationService(AppConfig appConfig, TranslationDAO dao, TranslationUtils utils) {
        this.appConfig = appConfig;
        this.dao = dao;
        this.utils = utils;
    }

    public UserResponse translateByThreads(UserRequest request) {
        String[] words = request.getText().split(" +");
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_CONST);
        translatedWords = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            int finalI = i;
            executorService.execute(() -> translateWord(words[finalI],
                    request.getOriginalLanguage(),
                    request.getFinalLanguage(),
                    finalI));
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                throw new ExecutorServiceException("Thread wait limit exceeded!");
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            throw new ExecutorServiceException("A thread was interrupted!");
        }

        String translatedText = String.join(" ", translatedWords);
//        dao.insert(new Translation(2,
//                utils.getIP(),
//                request.getText(),
//                request.getOriginalLanguage(),
//                translatedText,
//                request.getFinalLanguage(),
//                utils.getMoscowTime(),
//                200));
        return new UserResponse(translatedText, 200, "");
    }

    private void translateWord(String word, String fromLang, String toLang, int index) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        String url = String.format(
                appConfig.getTranslationURL(),
                word,
                fromLang,
                toLang);

        ResponseEntity<TranslateResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpEntity,
                TranslateResponse.class);

        translatedWords[index] = response
                .getBody()
                .getTranslatedText();
    }
}
