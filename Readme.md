# S4 Super Simple Scheduling System

## Technologies used
- Spring Web for REST API 
- Spring Data and JPA for persistence layer
- Spring Boot
- H2 in memory database

## Instructions to build and run it
- mvn clean package
- java -jar target/s4demo-0.0.1-SNAPSHOT.jar
- Browse REST API using Swagger UI: http://localhost:8080/s4demo/swagger-ui.html

## Examples on how to search for students

- To search students with id greater or equal than 2:

URL Request: 
POST /students/search

Request Body: 
[
  {
    "key": "id",
    "operation": ">",
    "value": "2"
  }
]


- To search a Student with first name "John" and last name "Doe":

URL Request: 
POST /students/search

Request Body: 
[
  {
    "key": "firstName",
    "operation": "=",
    "value": "jaime"
  },
{
    "key": "lastName",
    "operation": "=",
    "value": "tailor"
  }
]
