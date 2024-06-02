FROM maven:3.9 AS build
COPY config-server/src src
COPY config-server/pom.xml pom.xml
RUN mvn clean package -DskipTests

FROM openjdk:20-ea-4-jdk
LABEL authors="anatoly_dudko@icloud.com"
WORKDIR /app
COPY --from=build target/config-server-*.jar ./config-server.jar
EXPOSE 8888
CMD ["java", "-jar", "./config-server.jar", "--spring.profiles.active=docker"]