##Backend Test

Design and implement a RESTful API (including data model and the backing implementation) for
money transfers between accounts.

##Libraries:
- com.sparkjava:spark-core
- com.google.code.gson:gson
- ConcurrentHashMap as inMemory storage
- dependency injection through Constructor(in plan to add GoogleGuice)

##Endpoints
GET  /accounts
POST /accounts
GET  /accounts/:id
GET  /accounts/:id/balance
GET  /accounts/:id/transactions

GET  /transactions
POST /transactions
GET  /transactions/:id

## Build
    mvn clean package

## Run
    java -jar ./target/money-transferring-challenge-1.0.jar