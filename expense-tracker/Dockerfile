FROM maven:3.9 AS build
COPY expense-tracker/src src
COPY expense-tracker/pom.xml pom.xml
RUN mvn clean package -DskipTests

FROM openjdk:20-ea-4-jdk
LABEL authors="anatoly_dudko@icloud.com"
WORKDIR /app
COPY --from=build target/expense-tracker-*.jar ./expense-tracker.jar
EXPOSE 8083
CMD ["java", "-jar", "./expense-tracker.jar", "--spring.profiles.active=docker"]