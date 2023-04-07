FROM openjdk:11
ADD /target/test-0.0.1-SNAPSHOT.jar backendBot.jar
ENTRYPOINT "java", "-jar","backendBot.jar"