package com.tinkoff_lab.controllers;


import com.tinkoff_lab.requests.UserRequest;
import com.tinkoff_lab.responses.UserResponse;
import com.tinkoff_lab.services.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ektogod/translateText")
public class TranslationController {
    private TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping(consumes = "application/json",
            produces = "application/json")
    public UserResponse translate(@RequestBody UserRequest request) {
        return translationService.translateByThreads(request);
    }
}
