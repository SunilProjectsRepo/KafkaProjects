package com.kafka.domain;

import java.time.LocalDateTime;

public record Greeting(String message, LocalDateTime timestamp){}
