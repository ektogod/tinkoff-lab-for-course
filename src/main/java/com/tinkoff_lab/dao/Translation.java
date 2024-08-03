package com.tinkoff_lab.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Translation {
    private int id;
    private String ip;
    private String originalText;
    private String originalLang;
    private String translatedText;
    private String targetLang;
    private String time;
    private int status;
}
