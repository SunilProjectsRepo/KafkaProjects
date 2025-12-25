package com.kafka.topology;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.stream.Collectors;
@Slf4j
public class GreetingsTopology {

    //Source Topic Name 1
    public static String GREETINGS = "greetings";
    //Source Topic Name 2
    public static String GREETINGS_SPANISH = "greetings-spanish";
    //Destination Topic Name
    public static String GREETINGS_UPPERCASE = "greetings-uppercase";
    //Topology is  a class in KStreams which basically holds the whole flow of your KStreams.
    public static Topology buildTopology(){
        //Streamsbuilder is a building block using which you can define the Source processor and your Stream processing logic and then Sink processor
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        //Add the Source Processor - Pass Kafka Topic from where you want to read the message from. In addition to this, we need to add the key type and value type.
        //This will take care of getting the record from Kafka topic - GREETINGS
       /* var greetingsStream = streamsBuilder.stream(GREETINGS,
                Consumed.with(Serdes.String(), Serdes.String()));  // this means key is String and value is String
        */

        //Add the Source Processor
        //Providing Default Serializer/Deserializer Using Application Configuration
        KStream<String, String> greetingsStream = streamsBuilder.stream(GREETINGS);

        //To print the data in Stream
        greetingsStream.print(Printed.<String, String>toSysOut().withLabel("greetingsStream"));

        //Add the Source Processor
        //This will take care of getting the record from Kafka topic - GREETINGS_SPANISH
        /*var greetingsSpanishStream = streamsBuilder.stream(GREETINGS_SPANISH,
                Consumed.with(Serdes.String(), Serdes.String()));*/

        //Add the Source Processor
        //Providing Default Serializer/Deserializer Using Application Configuration
        KStream<String, String> greetingsSpanishStream = streamsBuilder.stream(GREETINGS_SPANISH);

        //To print the data in Stream
        greetingsSpanishStream.print(Printed.<String, String>toSysOut().withLabel("greetingsSpanishStream"));

        //Use the merge operator to combine these two KStreams - greetingsStream and greetingsSpanishStream
        var mergedStream = greetingsStream.merge(greetingsSpanishStream);

        //To print the data in Stream
        mergedStream.print(Printed.<String, String>toSysOut().withLabel("mergedStream"));

        //Build the processing logic - converting the value from lowercase to uppercase
		//Example to show filter, filterNot and mapValues usage
		var modifiedStream = mergedStream.mapValues(((readOnlyKey, value) -> value.toUpperCase()));

        //To print the data in Stream
        modifiedStream.print(Printed.<String, String>toSysOut().withLabel("modifiedStream"));

        //Add the Sink Processor - Publish this value to another topic
        //Pass Topic name, key type, value type
        /*modifiedStream.to(GREETINGS_UPPERCASE,
                Produced.with(Serdes.String(), Serdes.String()));*/

        //Add the Sink Processor
        //Providing Default Serializer/Deserializer Using Application Configuration
        modifiedStream.to(GREETINGS_UPPERCASE);

        //Returns the topology
        return streamsBuilder.build();

    }

}