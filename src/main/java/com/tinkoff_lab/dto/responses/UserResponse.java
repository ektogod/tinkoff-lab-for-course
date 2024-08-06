package com.tinkoff_lab.dto.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public record UserResponse(String translatedText) {       //class wth a "body" of response which sends to user
}
