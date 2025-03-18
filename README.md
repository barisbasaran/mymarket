# My Market - A Spring Boot reference application

## Overview

This is a Spring Boot reference application for managing a market. The application is containerized using Docker and supports multiple locales.

## Project Structure

- **src/main/java**: Contains the main application code.
- **src/main/resources**: Contains configuration files.
- **src/test/java**: Contains unit and integration tests.
- **src/test/resources**: Contains test configuration files.
- **web/static**: Contains static resources.

## Configuration

### Environment Variables

Environment variables are defined in the `.env` file. This file is not committed to the repository because it contains sensitive information such as passwords. These environment variables are referenced from:
- `src/main/resources/application.yml`
- `app container` environment section in the `docker-compose.yml` file

You have to add references to both of these files when you add a new environment variable in `.env`.

### Default Locale

Default locale is set to `tr-TR` in the `main.js` file.

### Supported Locales

Supported locales are configured in the `src/main/resources/application.yml` file.
To add a new locale:
1. Add the locale in the `application.yml` file.
2. Add a locale properties file in the `src/main/resources/messages` folder.

## Docker

Docker is used to run the application in a containerized environment. 

The application consists of 4 docker containers
1. `db` - The PostgreSQL database
2. `elasticsearch` - The Elastic Search database
3. `web` - The Nginx web server
4. `app` - The Spring Boot application

Please run the following to bring up the application in docker.

    docker-compose --profile local up

## Local setup

Install Nginx to your local. Then you may user the `dev/nginx/nginx.80.conf` file to configure Nginx to run the application locally.

If you want to have SSL, then use `dev/nginx/nginx.443.conf` file instead. You may also use the self signed certificate in the `dev/ssl` folder.

## SQL queries

You may find example SQL queries the `dev/sql` folder to run the SQL queries in the database.

## Web UI Layer

The web UI layer is served by the Nginx web server.

The web UI layer is built with **HTML** and **jQuery** and is located in the `web/static` folder.
We use vanilla JavaScript and jQuery to keep the code simple and easy to understand.

The page is rendered as pure **HTML** and then the dynamic content is filled with **jQuery** by making calls to the backend Spring Boot application.

For instance all translated text is fetched from the backend and then filled in the HTML page.

## Testing
The application is tested with **JUnit**, **Mockito** and **Testcontainers**. The tests are located in the `src/test/java` folder.

There are both unit tests and integration tests. You may run the integration tests with the following command.

    mvn -Pintegration test

## Security

The application uses **Spring Security** for authentication and authorization.
The security configuration is located in the `src/main/java/com/mymarket/web/WebSecurityConfig.java` file.

Authorization is done using URL patterns.

    .requestMatchers("/admin/**").hasRole(ADMIN.name())

It is also done using **Spring Security** annotations.

    @PreAuthorize("hasRole('STORE_OWNER')")
    public List<Product> getMyProducts()

The application uses **JWT** for authentication. 
The JWT token is generated when the user logs in and is sent back to the client. 
The client then sends the JWT token in the `Authorization` header for all subsequent requests.
