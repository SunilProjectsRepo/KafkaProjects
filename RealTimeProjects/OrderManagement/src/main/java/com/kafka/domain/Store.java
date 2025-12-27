package com.kafka.domain;

public record Store(String locationId,
                    Address address,
                    String contactNum) {
}
