FROM maven:3.9 AS build
COPY task-service/src src
COPY task-service/pom.xml pom.xml
RUN mvn clean package -DskipTests

FROM openjdk:20-ea-4-jdk
LABEL authors="anatoly_dudko@icloud.com"
WORKDIR /app
COPY --from=build target/task-service-*.jar ./task-service.jar
EXPOSE 8085
CMD ["java", "-jar", "./task-service.jar", "--spring.profiles.active=docker"]