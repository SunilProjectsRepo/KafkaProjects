package com.kafka.serdes;

import com.kafka.domain.Greeting;
import org.apache.kafka.common.serialization.Serde;

public class SerdesFactory {

    //This will take care of serializer and deserializer expected by the GreetingSerdes
    static public Serde<Greeting> greetingSerdes(){
        return new GreetingSerdes();
    }
}
