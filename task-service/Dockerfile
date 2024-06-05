FROM openjdk:20-ea-4-jdk
ADD task-service/target/task-service-*.jar task-service.jar
ENTRYPOINT ["java","-jar","task-service.jar"]