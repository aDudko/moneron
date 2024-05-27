FROM maven:3.9 AS build
COPY employee-service/src src
COPY employee-service/pom.xml pom.xml
RUN mvn clean package

FROM openjdk:20-ea-4-jdk
WORKDIR /app
COPY --from=build target/employee-service-0.0.1.jar ./employee-service.jar
EXPOSE 8082
CMD ["java", "-jar", "./employee-service.jar"]