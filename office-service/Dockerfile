FROM maven:3.9 AS build
COPY office-service/src src
COPY office-service/pom.xml pom.xml
RUN mvn clean package -DskipTests

FROM openjdk:20-ea-4-jdk
LABEL authors="anatoly_dudko@icloud.com"
WORKDIR /app
COPY --from=build target/office-service-*.jar ./office-service.jar
EXPOSE 8084
CMD ["java", "-jar", "./office-service.jar", "--spring.profiles.active=docker"]