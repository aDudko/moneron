FROM maven:3.9 AS build
COPY expense-tracker/src src
COPY expense-tracker/pom.xml pom.xml
RUN mvn clean package

FROM openjdk:20-ea-4-jdk
WORKDIR /app
COPY --from=build target/expense-tracker-0.0.2.jar ./expense-tracker.jar
EXPOSE 8083
CMD ["java", "-jar", "./expense-tracker.jar"]