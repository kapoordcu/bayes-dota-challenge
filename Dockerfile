FROM openjdk:10-jre-slim
COPY ./target/dota-challenge-1.0.0-SNAPSHOT.jar /usr/src/dota.jar
WORKDIR /usr/src
EXPOSE 8080
CMD ["java", "-jar", "dota.jar"]