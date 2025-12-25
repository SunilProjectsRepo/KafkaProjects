package com.kafka.launcher;

import com.kafka.topology.GreetingsTopology;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class GreetingsStreamApp {

    public static void main(String[] args) {
        Properties prop = new Properties();
        //Is an identifier of an application
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "greeting-app");
        //Bootstrap server configuration
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // I'm just going to read the records that's available in the Kafka topic. After this application is spun up, it's going to be latest.
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        //Providing Default Serializer/Deserializer Using Application Configuration
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);

        //Create the Topics
        createTopics(prop, List.of(GreetingsTopology.GREETINGS,GreetingsTopology.GREETINGS_UPPERCASE, GreetingsTopology.GREETINGS_SPANISH));
        //Get the Topology
        var greetingsTopology = GreetingsTopology.buildTopology();
        //Create the instance of Kafka Streams
        //This will execute the topology
        var kafkaStreams = new KafkaStreams(greetingsTopology, prop);

        //Any time you shut down this app, the shutdown hook is going to be invoked and then it's going to call the close function of KStreams.
        //This is equal to a graceful shutdown. It's going to take care of releasing all the resources that was accessed by this reading stream app.
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        try {
            //Start the kafkaStreams application
            kafkaStreams.start();
        }catch(Exception e){
            log.error("Exception in starting the Kafka Streams: {}",e.getMessage(),e);
        }
    }

    //This function create the Kafka Topics for us.
    private static void createTopics(Properties config, List<String> greetings) {

        AdminClient admin = AdminClient.create(config);
        var partitions = 1;
        short replication  = 1;

        //Creating the instance of topic
        var newTopics = greetings
                .stream()
                .map(topic ->{
                    return new NewTopic(topic, partitions, replication);
                })
                .collect(Collectors.toList());

        //Create the topic
        var createTopicResult = admin.createTopics(newTopics);
        try {
            createTopicResult
                    .all().get();
            log.info("topics are created successfully");
        } catch (Exception e) {
            log.error("Exception creating topics : {} ",e.getMessage(), e);
        }
    }
}
