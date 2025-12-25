package com.kafka.topology;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;

public class GreetingsTopology {

    //Source Topic Name
    public static String GREETINGS = "greetings";

    //Destination Topic Name
    public static String GREETINGS_UPPERCASE = "greetings-uppercase";
    //Topology is  a class in KStreams which basically holds the whole flow of your KStreams.
    public static Topology buildTopology(){
        //Streamsbuilder is a building block using which you can define the Source processor and your Stream processing logic and then Sink processor
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        //Add the Source Processor - Pass Kafka Topic from where you want to read the message from. In addition to this, we need to add the key type and value type.
        //This will take care of getting the record from Kafka topic
        var greetingsStream = streamsBuilder.stream(GREETINGS,
                Consumed.with(Serdes.String(), Serdes.String()));  // this means key is String and value is String
        //To print the data in Stream
        greetingsStream.print(Printed.<String, String>toSysOut().withLabel("greetingsStream"));

        //Build the processing logic - converting the value from lowercase to uppercase
		//Example to show filter, filterNot and mapValues usage
        /*var modifiedStream = greetingsStream
								//.filter((key,value) -> value.length() > 5)
								.filterNot((key,value) -> value.length() > 5)
								.mapValues(((readOnlyKey, value) -> value.toUpperCase()));*/
								
		//Build the processing logic - converting the value from lowercase to uppercase
		//Example to show map function usage
		var modifiedStream = greetingsStream
								.map((key,value) -> KeyValue.pair(key.toUpperCase(), value.toUpperCase()));
		
		
        //To print the data in Stream
        modifiedStream.print(Printed.<String, String>toSysOut().withLabel("modifiedStream"));

        //Add the Sink Processor - Publish this value to another topic
        //Pass Topic name, key type, value type
        modifiedStream.to(GREETINGS_UPPERCASE,
                Produced.with(Serdes.String(), Serdes.String()));

        //Returns the topology
        return streamsBuilder.build();

    }

}