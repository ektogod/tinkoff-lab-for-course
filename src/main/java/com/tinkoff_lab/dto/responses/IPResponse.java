package com.tinkoff_lab.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;


public record IPResponse(@JsonProperty("ip") String ip) {   // a record for storing response with current user external ip
}
