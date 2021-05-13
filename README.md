# distributed systems with load test

The main goal is to create project which represents a minimalistic microservice
architecture. This project is based on Java/Kotlin and gradle.

Required tools: **Gradle** , **Kotlin/Java**, **npm**, **Kubernetes**, **Jmeter** 
and **Docker**.

### What this app can do
Send post requests to the kafka broker over the gateway api, when a load on 
a CPU is getting to high the kubernetes adds additional instance to balance 
a load between multiply instances. There is also a consumer which prints out all
messages he receives. All this can be run inside the Kubernetes cluster to
emulate autoscaling under a high load.


### Structure

![alt text](structure.png "ms structure")


## Setup kafka with a GUI tool :rocket:

`docker-compose up --build
`

## Build and Run gateway api
`gradle build
`

`gradle run
`

## Build and Run a kafka consumer locally :rocket:
`cd consumer-microservice
`

`npm i
`

`node consumer.js
`

## Build and Run the frontend locally :rocket:
`npm start
`

## Interfaces overview

* Api gateway
* Kafka broker
* node consumer




