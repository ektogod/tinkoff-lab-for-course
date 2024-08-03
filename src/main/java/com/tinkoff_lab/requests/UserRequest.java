package com.tinkoff_lab.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String text;
    private String originalLanguage;
    private String finalLanguage;
}
