FROM fabric8/java-alpine-openjdk8-jre

ADD ["target/auth-0.0.1-SNAPSHOT.jar", "app.jar"]

CMD ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
