FROM openjdk:21-slim-bookworm

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 9000/tcp