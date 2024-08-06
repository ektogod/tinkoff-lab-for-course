package com.tinkoff_lab.dto;

public record Translation(String ip,       //// record for saving info about database records
                          String originalText,
                          String originalLang,
                          String translatedText,
                          String targetLang,
                          String time,
                          int status,
                          String message
) {
}
