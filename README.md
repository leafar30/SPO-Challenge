# SPO-Challenge

SPO Coding Challenge - Rafael Cornelio Bautista, 2019

[Download the build](https://github.com/leafar30/SPO-Challenge/blob/master/built/SPO-Challenge-1.0.0.jar)

# Purpose
This project builds to a Java 11 Spring Boot Rest Service, the input from a JSON Body and produces a response. Input represents a list of buildings to clean, and the abilities (how many rooms) of the staff sent to clean it. Output represents the number of staff (senior and junior) to send to each building and the number of Rooms in each building.

# Design

The code is organized in layers: Controller, Dtos, Manager, Service, Configuration files and Test Files

# Organization logic

The logic calculate the solutions for using more Sr than Jr, and the solution using more Jr than Sr. Then choose the more efficient one

# Code Features
* The service uses the Maven to build the project to handle dependencies, run tests and package it in a jarfile
* The controller endpoint are documented in the Swagger: http://localhost:8080/swagger-ui.html
* The service includes endpoints to know the status of the system using Actuator: http://localhost:8080/actuator/health
* The service has validations in the RequestBody for: required fields, not empty list, min and max for numeric values
* The service has logs for errors
* The service expose REST non-blocking (webflux) endpoints

# Test
* The project include Integration tests, for the controller (the 2 first test cases are the cases in the challenge description)
* The project include Unit Test, for the services and Manager
* The project include a Test for the Actuator status
* The project include a Test for the Swagger documentation
* The project has a class test coverage of 100%
* The project has a method test coverage of 94%
* The project has a line test coverage of 91%

# 3rd Party Libraries/Code
* SpringBoot
* Swagger
* Testing

# Build
* To build the app, first download the project from GitHub.
* You must configure the variables JAVA_HOME which points to the JDK home directory and MAVEN_PATH which points to the location of the maven executable
* Then run mvn clean install


# Run
* in the cmd console: 'mvn spring-boot:run' or run the com.spo.challenge.SpoChallengeApplication file
* curl --location --request POST 'http://localhost:8080/organize/portfolios' --header 'Content-Type: application/json' --data-raw '{"srCapacity":"10","jrCapacity":"6","structures":[35,21,17,28]}'
* curl --location --request POST 'http://localhost:8080/organize/portfolios' --header 'Content-Type: application/json' --data-raw '{"srCapacity":"11","jrCapacity":"6","structures":[24,28]}'


# Run PostMan
* in the cmd console: 'mvn spring-boot:run' or run the com.spo.challenge.SpoChallengeApplication file
* import the project collection 'SPO challenge.postman_collection.json'
* execute the requests


# Potential Improvements
If we find a third party library to calculate the optimization or a better algorithm, we can include the more implementations of the PortfolioOrganizer Interface and pass a parameter in the request to tell the PortfolioManager.java which implementation use.