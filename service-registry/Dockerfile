FROM maven:3.9 AS build
COPY service-registry/src src
COPY service-registry/pom.xml pom.xml
RUN mvn clean package -DskipTests

FROM openjdk:20-ea-4-jdk
LABEL authors="anatoly_dudko@icloud.com"
WORKDIR /app
COPY --from=build target/service-registry-*.jar ./service-registry.jar
EXPOSE 8761
CMD ["java", "-jar", "./service-registry.jar", "--spring.profiles.active=docker"]