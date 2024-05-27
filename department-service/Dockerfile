FROM maven:3.9 AS build
COPY department-service/src src
COPY department-service/pom.xml pom.xml
RUN mvn clean package

FROM openjdk:20-ea-4-jdk
WORKDIR /app
COPY --from=build target/department-service-0.0.1.jar ./department-service.jar
EXPOSE 8081
CMD ["java", "-jar", "./department-service.jar"]