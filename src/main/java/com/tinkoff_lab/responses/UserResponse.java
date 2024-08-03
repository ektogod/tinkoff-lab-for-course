package com.tinkoff_lab.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private String translatedText;
    private int responseStatus;
    private String responseDetails;
}
