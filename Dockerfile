FROM openjdk:23  AS builder

RUN mkdir /project

COPY . /project

WORKDIR /project

RUN ./mvnw clean package

FROM openjdk:23

RUN mkdir /app

COPY --from=builder /project/target/mymarket-0.0.1-SNAPSHOT.jar /app/mymarket.jar

WORKDIR /app

CMD ["java", "-jar", "mymarket.jar"]
