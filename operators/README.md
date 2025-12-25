# Set up Greeting App + Operators

This app is going to read from a Kafka topic named Greetings and then perform a stream processing logic and write it to another Kafka topic named Greetings uppercase. 

Just perform the different operators like filter, filterNot, map, mapValues operation and then write it to the Kafka topic.

<p align="center">
  <img src="src/main/resources/images/project-architecture.png" alt="Description of Project"/>
</p>

We have the Greeting as good morning and our Kafka streams app will read the message and perform the uppercase operation and write it to another Kafka topic named readings uppercase. 

This is a very simple and naive app.


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


#### Consume Messages

- This  command should take care of logging in to the Kafka container.

```
docker exec -it broker bash
```
- Command to consume messages from the Kafka topic.

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic greetings_uppercase
```

- Command to consume with Key

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic greetings_uppercase --from-beginning -property "key.separator= - " --property "print.key=true"
```

#### Producer side - Publish the message with key - "gm-Good morning"

<p align="center">
  <img src="src/main/resources/images/producer_key.png" alt="Producer"/>
</p>


#### KStreams App - where the map operation with key is done

<p align="center">
  <img src="src/main/resources/images/KStreams_App_with_key.png" alt="KStreams App Running"/>
</p>

#### Consumer side - consumes the message with key - "GM - GOOD MORNING"

<p align="center">
  <img src="src/main/resources/images/consumer_key.png" alt="Consumer"/>
</p>

### FlatMap Usage

<p align="center">
  <img src="src/main/resources/images/flatMapUsage.png" alt="FlatMap"/>
</p>

#### Producer side - Publish the message with key - "gm-Good"

<p align="center">
  <img src="src/main/resources/images/producer_flatMap.png" alt="Producer"/>
</p>


#### KStreams App - where the map operation with key is done

<p align="center">
  <img src="src/main/resources/images/KStreams_App_flatMap.png" alt="KStreams App Running"/>
</p>

#### Consumer side - consumes the message with key 

<p align="center">
  <img src="src/main/resources/images/consumer_flatMap.png" alt="Consumer"/>
</p>