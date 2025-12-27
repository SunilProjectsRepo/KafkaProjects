package com.kafka.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kafka.domain.Greeting;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class GreetingSerdes implements Serde<Greeting> {

    // We are going to register the time module because if you all can remember from the JSON, our JSON is going
    // to hold a timestamp and this is going to be represented using a local time if you are dealing with local daytime.
    // The latest Java library API the object mapper needs to provide some additional config in order to make sure
    // that gets parsed successfully.

    //This configuration is necessary in order to make sure that all the dates gets parsed successfully
    private final ObjectMapper objectMapper
            = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    @Override
    public Serializer<Greeting> serializer() {
        return new GreetingSerializer(objectMapper);
    }

    @Override
    public Deserializer<Greeting> deserializer() {
        return new GreetingDeserializer(objectMapper);
    }
}
