## Docker Setup

Docker is used to run the application in a containerized environment.

### Containers

The application consists of 4 docker containers
1. `db` - The PostgreSQL database
2. `elasticsearch` - The Elastic Search database
3. `web` - The Nginx web server
4. `app` - The Spring Boot application

The `app` container depends on the `db` and `elasticsearch` containers.
The `elasticsearch` dependency is based on the condition that the `elasticsearch` container is healthy.
    
    depends_on:
        elasticsearch:
            condition: service_healthy

The `db` dependency is based on the condition that the `db` container is started.

    depends_on:
      db:
        condition: service_started

The `web` container depends on the `app` container.
The `db` and `elasticsearch` containers are started first, followed by the `app` container, and finally the `web` container.

### Dockerfile

There are two Dockerfiles in the project:
1. `Dockerfile` - This is the Dockerfile for the Spring Boot application.
2. `web/Dockerfile` - This is the Dockerfile for the Nginx web server.

### Volumes

There's one shared volume called `app-volume`.

    volumes:
      - app-volume:/home

This volume is used to share files between the `app` and `web` containers.
It's mainly for sharing the `uploads` folder where the uploaded product images are uploaded and saved by the `app` container, and then they're served by the `web` container.

### Execution

You may run the following to bring up the application in docker.

    docker-compose --profile local up

### Environment variables

Environment variables are defined in the `.env` file.
These environment variables are referenced from the `docker-compose.yml` file

    environment:
      SPRING_DATASOURCE_URL: "jdbc:postgresql://db:5432/mydb"
      SPRING_DATASOURCE_USERNAME: ${SPRING_DATASOURCE_USERNAME}
      SPRING_DATASOURCE_PASSWORD: ${SPRING_DATASOURCE_PASSWORD}
      SEARCH_URL: "elasticsearch:9200"

You have to add references to the `docker-compose.yml` file when you add a new environment variable in `.env`.
