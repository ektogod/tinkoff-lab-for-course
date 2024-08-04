package com.tinkoff_lab.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequest {   // a "body" of user request
    private String text;
    private String originalLanguage;
    private String finalLanguage;
}
