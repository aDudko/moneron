FROM openjdk:20-ea-4-jdk
ADD department-service/target/department-service-*.jar department-service.jar
ENTRYPOINT ["java","-jar","department-service.jar"]