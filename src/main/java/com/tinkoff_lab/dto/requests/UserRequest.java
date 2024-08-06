package com.tinkoff_lab.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@Getter
//@AllArgsConstructor
public record UserRequest(String text, String originalLanguage, String finalLanguage) {   // a "body" of user request
}
