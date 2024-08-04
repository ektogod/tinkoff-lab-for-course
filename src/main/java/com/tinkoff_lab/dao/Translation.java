package com.tinkoff_lab.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Translation {    // class for saving info about database records
    //private int id;
    private String ip;
    private String originalText;
    private String originalLang;
    private String translatedText;
    private String targetLang;
    private String time;
    private int status;
}
