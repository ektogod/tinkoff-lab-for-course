package com.tinkoff_lab.services;

import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.dao.Translation;
import com.tinkoff_lab.exceptions.ExecutorServiceException;
import com.tinkoff_lab.exceptions.TranslationException;
import com.tinkoff_lab.requests.UserRequest;
import com.tinkoff_lab.dao.TranslationDAO;
import com.tinkoff_lab.responses.TranslateResponse;
import com.tinkoff_lab.responses.UserResponse;
import com.tinkoff_lab.utils.TranslationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class TranslationServiceImpl implements TranslationService {
    private static final int THREADS_CONST = 10;
    private String[] translatedWords;  // for storing translated words here

    private Logger logger = LoggerFactory.getLogger(TranslationServiceImpl.class);

    private AppConfig appConfig;   //for getting important data
    private TranslationDAO dao;     // for writing in database
    private TranslationUtils utils;

    @Autowired
    public TranslationServiceImpl(AppConfig appConfig, TranslationDAO dao, TranslationUtils utils) {
        this.appConfig = appConfig;
        this.dao = dao;
        this.utils = utils;
    }

    @Override
    public UserResponse translate(UserRequest request) {
        String[] words = request.getText().split(" +");
        translatedWords = new String[words.length];

        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_CONST);

        for (int i = 0; i < words.length; i++) {   // translating each word separately
            int finalI = i;
            var response = executorService.submit(() -> translateWord(
                            words[finalI],  // sending each word in a thread
                            request.getOriginalLanguage(),
                            request.getFinalLanguage(),
                            finalI));
            try {
                if (!HttpStatus.valueOf(response.get().getBody().getResponseStatus()).is2xxSuccessful()) {   // checking status - throwing exception if something wrong
                    Translation translation = new Translation(  // needs for writing in db in ExceptionHandler class
                            utils.getIP(),
                            request.getText(),
                            request.getOriginalLanguage(),
                            "",
                            request.getFinalLanguage(),
                            utils.getMoscowTime(),
                            response.get().getBody().getResponseStatus(),
                            response.get().getBody().getResponseDetails());

                    logger.error("Something goes wrong with translation for word <{}>, status is {}",
                            words[finalI],
                            response.get().getBody().getResponseStatus());

                    throw new TranslationException(response.get().getBody().getResponseDetails(), translation);
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Something goes wrong with thread: interruption or failing of execution occurred!");
                throw new ExecutorServiceException("Something goes wrong with thread: interruption or failing of execution occurred!");
            }
        }

        executorService.shutdown();
        try {    // if something goes wrong with threads
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                logger.error("Thread wait limit exceeded!");
                throw new ExecutorServiceException("Thread wait limit exceeded!");
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            logger.error("A thread was interrupted!");
            throw new ExecutorServiceException("A thread was interrupted!");
        }

        logger.info("Translation successfully completed");
        String translatedText = String.join(" ", translatedWords);
        dao.insert(new Translation(  // saving in database successful translation
                utils.getIP(),
                request.getText(),
                request.getOriginalLanguage(),
                translatedText,
                request.getFinalLanguage(),
                utils.getMoscowTime(),
                200,
                "Ok"));
        return new UserResponse(translatedText, 200, "");
    }

    private ResponseEntity<TranslateResponse> translateWord(String word, String fromLang, String toLang, int index) {
        logger.info("Sending translation request: word = {} ", word);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        String url = String.format(
                appConfig.getTranslationURL(),
                word,
                fromLang,
                toLang);

        ResponseEntity<TranslateResponse> response = restTemplate.exchange( // getting a response from translation api
                url,
                HttpMethod.POST,
                httpEntity,
                TranslateResponse.class);

        logger.info("Request has received: word = {} ", word);

        translatedWords[index] = response  // writing translated words to build a full translated text дфеук
                .getBody()
                .getTranslatedText();

        return response;
    }
}
