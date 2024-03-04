# "To-do" list app
An aplication to store and manage daily tasks.

## Table of content 
 - [General info](#general-info) 
 - [Technologies](#technologies)
 - [Features](#features)
 - [How to install and run](#how-to-install-and-run)
 - [Development Status](#development-status)
 
## General info
"To-do" backend application that helps to store information about tasks and deadlines. It allows to add, remove and change tasks in your "to-do" list. 

## How to install and run 
Clone the repository: 
> git clone https://github.com/YanaChe23/ToDoList.git

Go to the project folder:
> cd ToDoList

The app requires an encryption key for authorization. It's possible to run the app with a default key, however, it's also possible to provide the personal secret key to ensure a higher security level.

If you want to use the default key, run docker-compose.yml this way: 
> docker compose up -d 

If you want to provide a key, please add an environment variable SECRET_KEY:  
- for macOS/Linux
> export SECRET_KEY=place_your_key_here docker-compose up -d

- for Windows in CMD: 
> set SECRET_KEY=place_your_key_here && docker-compose up -d

Wait a couple of minutes and open API documentation to check if the project is started correctly: 
http://localhost:8080/swagger-ui/index.html#/

## Technologies
- Java 17 
- Maven
- Spring Boot 
- Spring MVC
- Spring Data JPA
- Spring Security
- Swagger Codegen 
- Docker
- PostgreSQL
- Liquibase
- JUnit 5
- Testcontainers

## Features
The application allows to perform CRUD operations with tasks, such as adding and deleting tasks, getting list of available tasks or searching by ID, filtering by deadline etc. 
Please check API documentation for more information (awailable once application is up): http://localhost:8080/swagger-ui/index.html#/

## Development status
##### Under Development
Please note that this application is currently under active development. 
##### Available functionality 
All CRUD operations dedicated to tasks are available. 
#####  Upcoming Features
- User authorization; 
- Multi-user capabilities: store tasks of sevral users.
