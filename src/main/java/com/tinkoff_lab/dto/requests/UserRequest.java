package com.tinkoff_lab.dto.requests;

public record UserRequest(String text, String originalLanguage, String finalLanguage) {   // a "body" of user request
}
