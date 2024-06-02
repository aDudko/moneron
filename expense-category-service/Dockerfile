FROM maven:3.9 AS build
COPY expense-category-service/src src
COPY expense-category-service/pom.xml pom.xml
RUN mvn clean package -DskipTests

FROM openjdk:20-ea-4-jdk
LABEL authors="anatoly_dudko@icloud.com"
WORKDIR /app
COPY --from=build target/expense-category-service-*.jar ./expense-category-service.jar
EXPOSE 8086
CMD ["java", "-jar", "./expense-category-service.jar", "--spring.profiles.active=docker"]