FROM maven:3.9 AS build
COPY auth-service/src src
COPY auth-service/pom.xml pom.xml
RUN mvn clean package

FROM openjdk:20-ea-4-jdk
WORKDIR /app
COPY --from=build target/auth-service-0.0.1.jar ./auth-service.jar
EXPOSE 8087
CMD ["java", "-jar", "./auth-service.jar"]