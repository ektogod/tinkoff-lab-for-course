package com.tinkoff_lab.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IPResponse {
    @JsonProperty("ip")
    private String ip;
}
