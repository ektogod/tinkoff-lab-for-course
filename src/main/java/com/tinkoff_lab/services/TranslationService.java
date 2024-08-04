package com.tinkoff_lab.services;

import com.tinkoff_lab.requests.UserRequest;
import com.tinkoff_lab.responses.UserResponse;

public interface TranslationService {
    public UserResponse translate(UserRequest request);
}
