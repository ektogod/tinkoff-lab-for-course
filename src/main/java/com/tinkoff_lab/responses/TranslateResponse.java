package com.tinkoff_lab.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TranslateResponse {    // class for storing response from translation API
    //@JsonProperty("translatedText")
    private String translatedText;

    @JsonProperty("responseStatus")
    private int responseStatus;

    @JsonProperty("responseDetails")
    private String responseDetails;

    @JsonProperty("responseData")
    private void getTranslatedTextFromJSON(Map<String, Object> responseData) {
        this.translatedText = (String) responseData.get("translatedText");
    }
}
