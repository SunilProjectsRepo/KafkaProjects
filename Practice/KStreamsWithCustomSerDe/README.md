# Build Custom SerDe for Enhanced Greeting Messages with merge operation

## What is needed to build a Custom Serde?
 - Serializer
 - DeSerializer
 - Serde that holds the Serializer and DeSerializer


We will have 3 topics here:
GREETINGS, GREETINGS_SPANISH - producer 
GREETINGS_CUSTOM_SERDE - consumer


## Set up Kafka Environment using Docker

- This should set up the Zookeeper and Kafka Broker in your local environment

```aidl
docker-compose up
```

### Verify the Local Kafka Environment

- Run this below command

```
docker ps
```

- You should be below containers up and running in local

<p align="center">
  <img src="src/main/resources/images/running_container.png" alt="Running containers"/>
</p>


### Interacting with Kafka

#### Produce Messages

- This  command should take care of logging in to the Kafka container.

```
docker exec -it broker bash
```

- Command to produce messages in to the Kafka topic.

```
kafka-console-producer --broker-list localhost:9092 --topic greetings
```

- Publish to **greetings** topic with key and value

```
kafka-console-producer --broker-list localhost:9092 --topic greetings --property "key.separator=-" --property "parse.key=true"

```

- Publish to **greetings-spanish** topic with key and value

```
 kafka-console-producer --broker-list localhost:9092 --topic greetings_spanish --property "key.separator=-" --property "parse.key=true"
```


#### Consume Messages

- This  command should take care of logging in to the Kafka container.

```
docker exec -it broker bash
```
- Command to consume messages from the Kafka topic.

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic greetings-customserde
```

- Command to consume with Key

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic greetings-customserde --from-beginning -property "key.separator= - " --property "print.key=true"
```

#### Producer side - Publish the message in topic - GREETINGS and GREETINGS-SPANISH through GreetingMockDataProducer class

<p align="center">
  <img src="src/main/resources/images/producer_customserde.png" alt="Producer"/>
</p>


#### KStreams App 

<p align="center">
  <img src="src/main/resources/images/KStreams_App_with_custom_serde.png" alt="KStreams App Running"/>
</p>

#### Consumer side - consumes the message with key from both topics as Greeting object

<p align="center">
  <img src="src/main/resources/images/consumer_customserde.png" alt="Consumer"/>
</p>
