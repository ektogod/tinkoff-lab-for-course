package com.tinkoff_lab.services;

import com.tinkoff_lab.dto.requests.UserRequest;
import com.tinkoff_lab.dto.responses.UserResponse;

public interface TranslationService {
    UserResponse translate(UserRequest request);
}
