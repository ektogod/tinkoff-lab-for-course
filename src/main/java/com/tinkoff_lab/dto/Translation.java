package com.tinkoff_lab.dto;

import lombok.*;



public record Translation(String ip,       //// recrd for saving info about database records
                          String originalText,
                          String originalLang,
                          String translatedText,
                          String targetLang,
                          String time,
                          int status,
                          String message
) {
    //private int id;
}
