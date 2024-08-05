package com.tinkoff_lab.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserRequest {   // a "body" of user request
    private String text;
    private String originalLanguage;
    private String finalLanguage;
}
