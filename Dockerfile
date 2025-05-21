FROM openjdk:21-jdk-slim-buster

COPY ./target/pedido-*.jar application.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/application.jar"]