FROM maven:3.9 AS build
COPY staff-service/src src
COPY staff-service/pom.xml pom.xml
RUN mvn clean package

FROM openjdk:20-ea-4-jdk
WORKDIR /app
COPY --from=build target/staff-service-0.0.1.jar ./staff-service.jar
EXPOSE 8082
CMD ["java", "-jar", "./staff-service.jar"]