package com.tinkoff_lab.exceptions;

import com.tinkoff_lab.dao.Translation;
import lombok.Getter;

@Getter
public class TranslationException extends RuntimeException{  //exception in case of troubles with text translation
    private Translation translation;
    public TranslationException(String message, Translation translation) {
        super(message);
        this.translation = translation;
    }
}
