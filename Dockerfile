FROM openjdk:17-jdk-slim

COPY ./target/user-manager-app-0.0.1-SNAPSHOT.jar user-manager-app-0.0.1-SNAPSHOT.jar

CMD ["java","-jar","user-manager-app-0.0.1-SNAPSHOT.jar"]