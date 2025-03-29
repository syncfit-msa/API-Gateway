FROM openjdk:17-ea-11-jdk-slim
WORKDIR /app
COPY build/libs/apigateway-0.0.1-SNAPSHOT.jar api-gateway.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]