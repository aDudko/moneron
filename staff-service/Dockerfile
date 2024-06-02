FROM maven:3.9 AS build
COPY staff-service/src src
COPY staff-service/pom.xml pom.xml
RUN mvn clean package -DskipTests

FROM openjdk:20-ea-4-jdk
LABEL authors="anatoly_dudko@icloud.com"
WORKDIR /app
COPY --from=build target/staff-service-*.jar ./staff-service.jar
EXPOSE 8082
CMD ["java", "-jar", "./staff-service.jar", "--spring.profiles.active=docker"]