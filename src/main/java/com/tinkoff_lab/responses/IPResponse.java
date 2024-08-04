package com.tinkoff_lab.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IPResponse {   // a class for storing response with current user external ip
    @JsonProperty("ip")
    private String ip;
}
