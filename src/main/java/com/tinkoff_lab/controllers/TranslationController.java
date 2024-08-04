package com.tinkoff_lab.controllers;


import com.tinkoff_lab.requests.UserRequest;
import com.tinkoff_lab.responses.UserResponse;
import com.tinkoff_lab.services.TranslationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ektogod/translateText")

public class TranslationController {                 // class for receiving requests from users
    private TranslationServiceImpl translationService;
    private Logger logger = LoggerFactory.getLogger(TranslationController.class);

    @Autowired
    public TranslationController(TranslationServiceImpl translationService) {
        this.translationService = translationService;
    }

    @GetMapping(consumes = "application/json",
            produces = "application/json")

    public UserResponse translate(@RequestBody UserRequest request) {
        logger.info("Request from user has received: {}", request);
        UserResponse response =  translationService.translate(request);
        logger.info("Response is sending to user");
        return response;
    }
}
