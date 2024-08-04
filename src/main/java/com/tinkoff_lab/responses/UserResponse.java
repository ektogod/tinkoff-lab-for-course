package com.tinkoff_lab.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {       //class wth a "body" of response which sends to user
    private String translatedText;
    private int responseStatus;
    private String responseDetails;
}
