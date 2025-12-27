package com.kafka.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.domain.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
public class GreetingDeserializer implements Deserializer<Greeting> {
    //ObjectMapper is part of Jackson that is going to perform the serialization and deserialization for custom serdes
    private ObjectMapper objectMapper;

    public GreetingDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    //This is primarily use when we are consuming a message from Kafka Topic
    @Override
    public Greeting deserialize(String topic, byte[] data) {
        try {
            //Bytes → Objects
            return objectMapper.readValue(data, Greeting.class);
        } catch (IOException e) {
            log.error("IOException : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
