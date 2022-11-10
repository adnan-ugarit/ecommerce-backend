FROM adoptopenjdk/openjdk8:ubuntu-jre

RUN apt-get update && apt-get -y install netcat

ADD ["target/bank-0.0.1-SNAPSHOT.jar", "app.jar"]

CMD ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
