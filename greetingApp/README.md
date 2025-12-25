# Set up Greeting App

This app is going to read from a Kafka topic named Greetings and then perform a stream processing logic and write it to another Kafka topic named Greetings uppercase. 

Just perform the uppercase operation and then write it to the Kafka topic.

<p align="center">
  <img src="src/main/resources/images/project-architecture.png" alt="Description of Project"/>
</p>

We have the Greeting as good morning and our Kafka streams app will read the message and perform the uppercase operation and write it to another Kafka topic named readings uppercase. 

This is a very simple and naive app.