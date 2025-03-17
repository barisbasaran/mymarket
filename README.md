# My Market - A Spring Boot reference application

### Default Locale  

Default locale is set to `tr-TR` in the `main.js` file.

### Supported Locales

Supported Locales are configured in the `src/main/resources/application.yml` file.
To add a new locale, add the locale in the `application.yml` file and then add a locale properties file in the `src/main/resources/messages` folder. 

### Environment Variables

Environment Variables are defined in the `.env` file. This file is not committed to the repository because it contains sensitive information such as passwords.

These environment variables are then referenced from the `src/main/resources/application.yml` file and the `app container` environment section in the `docker-compose.yml` file.
You have to add references to both of these files when you add a new environment variable in `.env`.

### Docker

Docker is used to run the application in a containerized environment. 

The application consists of 4 docker containers
1. `db` - The PostgreSQL database
2. `elasticsearch` - The Elastic Search database
3. `web` - The Nginx web server
4. `app` - The Spring Boot application

Please run the following to bring up the application in docker.

    docker-compose --profile local up

### Local setup

Install Nginx to your local. Then you may user the `dev/nginx/nginx.80.conf` file to configure Nginx to run the application locally.

If you want to have SSL, then use `dev/nginx/nginx.443.conf` file instead. You may also use the self signed certificate in the `dev/ssl` folder.

### SQL queries

You may find example SQL queries the `dev/sql` folder to run the SQL queries in the database.

### Web UI Layer

The web UI layer is served by the Nginx web server.

The web UI layer is built with **HTML** and **jQuery** and is located in the `web` folder.
We use vanilla JavaScript and jQuery to keep the code simple and easy to understand.

The page is rendered as pure **HTML** and then the dynamic content is filled with **jQuery** by making calls to the backend Spring Boot application.

For instance all translated text is fetched from the backend and then filled in the HTML page.

### Testing
The application is tested with **JUnit**, **Mockito** and **Testcontainers**. The tests are located in the `src/test/java` folder.

There are both unit tests and integration tests. You may run the integration tests with the following command.

    mvn -Pintegration test
