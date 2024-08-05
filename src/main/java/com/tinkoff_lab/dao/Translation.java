package com.tinkoff_lab.dao;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class Translation {    // class for saving info about database records
    //private int id;
    private String ip;
    private String originalText;
    private String originalLang;
    private String translatedText;
    private String targetLang;
    private String time;
    private int status;
    private String message;
}
