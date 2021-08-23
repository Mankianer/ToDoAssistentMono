# ToDo Assistent Mono

Java Spring Boot Application with Rest Endpoints and Dgraph Database with Angular UI.

## build and run

### backend

use gradle:

```
gradlew build

gradlew run
```

### UI

user npm in UI/todo-assistent-ui directory

```shell
UI/todo-assistent-ui/npm install

UI/todo-assistent-ui/npm start

UI/todo-assistent-ui/ng build
```

## Status

v0.1.0

### Calendar

added GoogleCalender Basics

### Google OAuth

added GoogleOAuth for requesting user information

### JWT Security on Endpoints

added JWT Token for access.  
added Secure Cookie via HttpOnly Cookie in Browsers added /dev/hallo endpoint for getting
EndlessToken

### UI

added simple Angular UI added test a Login

## SetUp

steps to be done bevor running correctly:

* Set GoogleApiCredentials Property: `GOOGLE_APPLICATION_CREDENTIALS=/path/credentials.json`  
* SetUp DGraph DB ([docker_compose.yml](dev/docker_compose/dgraph/docker-compose.yml)) 
