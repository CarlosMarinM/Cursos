package com.carlosmarin.kafka.models;

import lombok.Data;

@Data
public class Farewell {
    private String message;
    private Integer remainingMinutes;
}
