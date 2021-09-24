# Getting Started

##### This Spring application was initialized with `https://start.spring.io/`

## Purpose of this application

- This application is part of a POC which aims the at following
  - Connecting multiple Docker-based services to establish a data source for Grafana dashboards where monitoring of cryptocurrency information shall take place
  - This Maven-based Spring Boot backend application shall provide a REST API that handles CRUD operations persisted in a PostgreSQL database for Cryptocurrency information such as price performance over time
  - For price updates, a separate crypto client is in place (the client is covered separately), which serves as the man in the middle between the cryptocurrency web service (in this case, coingecko.com) and this Spring Boot backend server

> **Note:** In case there are any compile issues, I used [OpenJDK 11 with OpenJ9 JVM](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=openj9) for this application.

> **Note:** The following description contains the steps you need to take starting the application and also helpful background information, which I prefix with [INFO], essential steps you need to take care of are highlighted with [IMPORTANT]

## Prerequisites

- You have Java installed. Check above which version I have used in case of encountering issues
- You have Docker installed on your machine
- You have Maven installed
- Git bash (if you are on Windows)

### Starting the application

- Before this application can be started, there are some steps to be taken, such as setting up a local docker database described in the following steps. However, the class you will need eventually to run is: "CoinApplication.java" but step by step. Here are some general Infos which you should notice:
  - [INFO] The application runs on `localhost:8080/`
  - [INFO] You may check later which Database implementation is used in `CoinService.java` - check the description below how to swap the implementation
  - [INFO] If a PostgresDB is used, make sure the Docker Container with the DB is started accordingly - check the description below how to achieve that

### Architecture

![Architecture](src/main/resources/Architecture.png)

### [INFO] SWAP Database implementation via Dependecy Injection

- Class `CoinService.java`
- Line #22 in Constructor `CoinService`
- Add `@Qualifier` Annotation
- This replaces the Interface with a chosen impmlementation
- Example `public CoinService(@Qualifier("postgres") CoinDao coinDao)`

> **Note:** Look which classes implement the CoinDao Interface, for instance, CoinDataAcessService

### [INFO] Logging

- Logging behavior is defined in `src/main/resources/logback-spring.xml`
- Here, two profiles are created `dev` and `prod`
- `prod` logs to a log file `app.log` located in folder `logs`
- `dev` logs into the console
- Either profile can be activated in `src/main/resources/application.properties`

### [IMPORTANT] Database Definition in Project

- See the file `src/main/resources/application.yml` -> Contains Database Connection Properties, make sure that the data provided here will match with the data you will use in the following steps creating a database
- This File is referenced in class `PostgresDatasource.java` in line #14
  > **Note:** Find detailed information what is done here: https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto-configure-a-datasource
  > The Section '9.1. Configure a Custom DataSource' contains all information required, also what happens in file `src/main/resources/application.yml` search in the spring docs for "app.datasource.jdbc-url=jdbc:mysql://localhost/test" to navigate to the correct position
  > Flyway comes as an out-of-the-box integration plugin, find more details here: https://flywaydb.org/documentation/plugins/springboot

### [INFO] Create or Update a DB Table within your IDE

1. Create folders `db/migration` within `src/main/resources` in the project.
2. Create a File named `V1__<Some Name>Table.sql`
   > **Note:** By default, Flyway looks at files in the format V$X__$DESCRIPTION.sql, where $X is the migration version name, in folder src/main/resources/db/migration.
   > Go to https://flywaydb.org/documentation/api/ to find more details
3. Insert the following SQL command
   `CREATE TABLE person ( id UUID NOT NULL PRIMARY KEY, name VARCHAR(100) NOT NULL );`

### [IMPORTANT] Initial Start a Postgres DB in a Docker Container

`docker run --name <your_docker_postgres_db_name> -e POSTGRES_PASSWORD=password -d -p 5432:5432 postgres:alpine`

> **Note:** Do not use Git Bash for the following commands
> **Note:** The default Postgres Port is 5432. The param -p maps the internal port to an external port.

### [INFO] List all containers

`docker ps -a`

### Start a stopped Docker Container Postgres DB

`docker start /<your_docker_postgres_db_name>`

> **Note:** The command `docker run` should have been already applied, and the container must be stopped and should be available for start

### [IMPORTANT] Show running Docker files to find your <your_docker_postgres_db_name> Container ID

`docker ps`

### [IMPORTANT] Enter the Docker Container via command-line interface

`docker exec -it <CONTAINER ID> bin/bash`

> **Note:** execute docker ps first to retrieve the CONTAINER ID
> **Note:** The result should look something like this:
> `bash-5.0#`

### [IMPORTANT] Access the Postgres DB

`bash-5.0# psql -U postgres`

### [INFO] List all Databases

`postgres=# \l`

### [IMPORTANT] Create a Database

`postgres=# CREATE DATABASE <databasename>;`

> **Note:** The database name in this example is `coin`

### [IMPORTANT] Connect to the created Database

`postgres=# \c <databasename>`

> **Note:** in this example: `postgres=# \c coin`

### [INFO] Show all Databases

`postgres=# \d`

### [INFO] Show all Database Tables

`postgres=# \dt`

### [IMPORTANT] Install UUID Generation Extension to Postgres DB

`postgres=# CREATE EXTENSION "uuid-ossp";`

### [IMPORTANT] Test UUID Extension in Postgres DB. This should output something

`postgres=# SELECT uuid_generate_v4();`

### [INFO] Query all Content from Table `coin`

`SELECT * FROM coin;`

### [IMPORTANT] Exit the DB for now by typing

`exit` until you have exited the DB

### [IMPORTANT] I recommend administering the DB via pgAdmin since it is straightforward to use and more user friendly

`docker pull dpage/pgadmin4`

### [IMPORTANT] Run pgAdmin

`docker run -p 80:80 -e "PGADMIN_DEFAULT_EMAIL=<your mail address>" -e "PGADMIN_DEFAULT_PASSWORD=password" -d dpage/pgadmin4`

### [IMPORTANT] Access pgAdmin from Browser

`localhost:80`

### [IMPORTANT] Login to pgAdmin from Browser

Username: `<your mail address>`
PW: `password`

### [IMPORTANT] Add Postgres Server to pgAdmin

> **Note:** In case you want to access the Postgres Database, which runs in the Docker Container from within pgAdmin

- Klick "Add New Server"
- Klick Tab "Connection"
- Add the following properties

> Host name/address: `host.docker.internal`

> Port: `5432`

> Maintenance database: `coin`

> User name: `postgres`

> Password: `password`

- Click Save
- You should now be connected

![Architecture](src/main/resources/pgAdmin.png)

### [IMPORTANT] Check that the src/main/resources/application.yml file has

- the correct port in jdbc-url (5432) in this example
- the correct suffix in jdbc-url (coin) for this example
- the correct username (postgres) in this example
- the correct password (password) in this example

### [IMPORTANT] Run CoinApplication.java

- If the Database is running and exposed to port 5432 you should run the application
- You can check whether the application is reachable by firing a get request to localhost:8080/
- You should see the following message: "Crypto Coin API is served under path: /api/v1/coin"
- You can now also use Postman to post some data to the API.
  - Create a new POST Request
  - enter `localhost:8080/api/v1/coin`
  - paste the following content into the Body field:
  - `{ "id" : "24649f69-f963-4358-8df5-7716cd5ca520", "timestamp" : 1631728657000, "symbol" : "algo", "coinId" : "algorand", "coinName" : "Algorand", "priceEur" : 1.75, "priceUsd" : 2.06, "priceBtc" : 0.00004278, "priceEth" : 0.00058266, "marketCapEur" : 9129540478.621513, "marketCapUsd" : 10782459113.315432, "marketCapBtc" : 223272.92629636533, "marketCapEth" : 3044854.898276552, "totalVolumeEur" : 630063059.8750135, "totalVolumeUsd" : 744086064.2790741, "totalVolumeBtc" : 15443.646497872505, "totalVolumeEth" : 210319.25611616662, "twitterFollowers" : 0, "redditAvgPosts48Hours" : null, "redditAvgComments48Hours" : null, "redditAccountsActive48Hours" : null, "redditSubscribers" : 0, "devForks" : 0, "devStars" : 0, "devTotalIssues" : 0, "devClosedIssues" : 0, "devPullRequestsMerged" : 0, "devPullRequestContributors" : 0, "devCommitCount4Weeks" : 0, "devCodeAdditions4Weeks" : 0, "devCodeDeletions4Weeks" : 0, "publicAlexaRank" : 0 }`
- Pray :D, now if everything worked fine, you should receive a 200 response, and a new entry should be made into the Postgres DB table "algorand"

### [IMPORTANT] Refresh pgadmin to verify if the DB scripts have been applied

- Reaccess pgadmin and refresh coin
- Drill down to coin > coin > Schemas > Tables, and you should see ~ 25 Tables that have been created
- However, there is no Data. If you would like to continue with the data I have already downloaded, you should apply the following database backup

### [RECOMMENDED] I have only tested the application of my backup to a clean database thus, I recommend the following (But I think it should also work for coin)

- In pgAdmin, create a new database by right click n the elephant symbol "coin" > create > Database...
- Call it "cryptobackup" for instance
- in misc/dbbackup/coinbackup.sql is the file (I will probably zip it later, so unzip it if so). This is the backup you should apply to your newly created Database with the following command:

  `cat coinbackup.sql | docker exec -i <your_docker_postgres_container_name> psql -U postgres -d cryptobackup`

- If everything works, you should be able to refresh in cryptobackup in pgAdmin and see content in the tables.
- NOTE: If you want to use this Database from now with the Spring Boot API, don't forget to adjust "application.yaml" accordingly by changing the URL suffix from "coin" to "cryptobackup"

### [INFO] You may want to build a Docker Container for this Spring Boot application

- go into the project directory
- execute:

  `docker build -f Dockerfile -name docker-spring-boot -t docker-spring-boot .`

### [INFO] Run this application in a Docker Container

`docker run -d -p 8080:8080 --name docker-spring-boot docker-spring-boot`

> **Note:** Running the app in a Docker Container requires a change in 'application.yaml' to find the Database, the following line needs to be entered:
> `jdbc-url: jdbc:postgresql://host.docker.internal:5432/cryptobackup`

### [RECOMMENDED]

- To connect the DB to Grafana, you need a running Docker Grafana Container
- Run the following command for that:

`docker run -d --name=grafana -p 3000:3000 grafana/grafana`

- open `localhost:3000` in your browser and use user/pw: `admin`
- Now you need to plug in the datasource
  - Click on the gear wheel on the left side > Configuration > Data Sources > Add data source
  - Choose PostgreSQL
  - Give it some name
    - Host: `host.docker.internal:5433`
    - Database: `cryptobackup`
    - User: `postgres`
    - Password: `password`
    - SSL Mode: `disable` (since it runs only local this is ok)
  - Click "Save & Test"
  - It should be able to communicate to the DB

![Architecture](src/main/resources/Grafana_DS.png)

- Now, you will still not see anything. This is because you are missing the Dashboards.
  - In Grafana click on the "+" symbol in the left menu > Import > Upload JSON File
  - Now choose `misc/grafana/<some dashboard name>.json`, which I have provided, and upload it
  - Once you have all uploaded, you should finally have the dashboards available in Grafana
  - You should now be able to open a dashboard and play around with the data :)

![Architecture](src/main/resources/Grafana_Dashboard.png)

### [INFO] Fetching Coin Data

- You may want to update coin information, for this purpose [crypto-client-poc](https://github.com/src-dbgr/crypto-client-poc) is available

### [INFO] Start / Stop Stack

- For simplicity, I have added some simple batch scripts which start
- Stack contains: Postgres DB Docker Container, Spring Boot App Docker Container), Grafana Docker Container
  Start: `misc\start-docker.bat`
  Stop: `misc\stop-docker.bat`
