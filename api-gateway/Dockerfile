FROM maven:3.9 AS build
COPY api-gateway/src src
COPY api-gateway/pom.xml pom.xml
RUN mvn clean package -DskipTests

FROM openjdk:20-ea-4-jdk
LABEL authors="anatoly_dudko@icloud.com"
WORKDIR /app
COPY --from=build target/api-gateway-*.jar ./api-gateway.jar
EXPOSE 9191
CMD ["java", "-jar", "./api-gateway.jar", "--spring.profiles.active=docker"]