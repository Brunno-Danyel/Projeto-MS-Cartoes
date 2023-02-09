FROM openjdk:17
WORKDIR /app
COPY ./target/eurekaServer-1.0-SNAPSHOT.jar eurekaServerApp.jar
EXPOSE 8761
ENTRYPOINT java -jar eurekaServerApp.jar