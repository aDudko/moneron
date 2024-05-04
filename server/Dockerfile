FROM maven:3.9 AS build
COPY server/src src
COPY server/pom.xml pom.xml
RUN mvn clean package

FROM openjdk:20-ea-4-jdk
WORKDIR /app
COPY --from=build target/moneron-0.0.1.jar ./app.jar
EXPOSE 8081
CMD ["java", "-jar", "./app.jar"]