# Crypto Monitor API

This project is a Spring Boot application for monitoring cryptocurrency information.

## Prerequisites

- Docker
- Docker Compose
- Java 21
- Maven

## Setup

1. Clone the repository:

2. Start the PostgreSQL database and pgAdmin:
   ```
   docker-compose up -d
   ```

3. Wait for about 10-15 seconds to ensure the PostgreSQL container is fully up and running.

4. Connect to the PostgreSQL container:
   ```
   docker exec -it postgres-coin psql -U postgres
   ```

5. Create the 'coin' database:
   ```sql
   CREATE DATABASE coin;
   ```

6. Connect to the 'coin' database:
   ```sql
   \c coin
   ```

7. Create the UUID extension:
   ```sql
   CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
   ```

8. Exit the PostgreSQL prompt:
   ```
   \q
   ```

9. Add some data to the Database.
   9.1. cd into `crypto-monitoring-api-poc/misc/dbbackup`
   9.2. unpack `coinbackup.rar`
   9.3. add data to `coin` database
   ```
   docker exec -i postgres-coin psql -U postgres -d coin < coinbackup.sql
   ```

10. Build the Spring Boot application:
   ```
   mvn clean install -DskipTests
   ```

11. Run the Spring Boot application:
    ```
    java -jar target/coin-0.0.1-SNAPSHOT.jar
    ```

## Set Up Grafana

1. Add a new PostgreSQL Datasource
2. Click on `Build a dashboard`
3. Choose "Import dashboard"
4. Choose "Upload dashboard JSON file"
5. Select a JSON file from `crypto-monitoring-api-poc\misc\grafana\<some-file-name>.json`
6. Click on "Import"
7. 

## Accessing pgAdmin

1. Open a web browser and go to `http://localhost:5050`
2. Login with:
    - Email: `user@example.com`
    - Password: `password`

3. Add a new server in pgAdmin:
    - Host: `postgres`
    - Port: `5432`
    - Maintenance database: `coin`
    - Username: `postgres`
    - Password: `password`

## API Usage

The API should now be running on `http://localhost:8080`. You can use tools like Postman or curl to interact with the endpoints defined in your controllers.

## Stopping the Application

1. Stop the Spring Boot application (Ctrl+C in the terminal where it's running)
2. Stop the Docker containers:
   ```
   docker-compose down
   ```

## Additional Notes

- The specific table structures and schemas will be created by your Spring Boot application using JPA/Hibernate when it first runs.
- Make sure your `application.yml` or `application.properties` file in the Spring Boot project is configured to connect to the PostgreSQL database:

  ```yaml
    app:
      datasource:
        jdbc-url: jdbc:postgresql://localhost:5432/coin
        username: postgres
        password: password
        pool-size: 30
    
    spring:
      data:
        mongodb:
          uri: mongodb://mongoadmin:password@localhost:27017/coin_candles?authSource=admin
  ```

- If you need to make any manual changes to the database schema or add initial data, you can do so by connecting to the database using the steps 4-8 above.