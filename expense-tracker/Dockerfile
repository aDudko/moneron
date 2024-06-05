FROM openjdk:20-ea-4-jdk
ADD expense-tracker/target/expense-tracker-*.jar expense-tracker.jar
ENTRYPOINT ["java","-jar","expense-tracker.jar"]