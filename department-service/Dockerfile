FROM maven:3.9 AS build
COPY department-service/src src
COPY department-service/pom.xml pom.xml
RUN mvn clean package -DskipTests

FROM openjdk:20-ea-4-jdk
LABEL authors="anatoly_dudko@icloud.com"
WORKDIR /app
COPY --from=build target/department-service-*.jar ./department-service.jar
EXPOSE 8081
CMD ["java", "-jar", "./department-service.jar", "--spring.profiles.active=docker"]