# Compile jar file
FROM ubuntu:latest AS project-build
RUN echo $SERVER_PORT

RUN apt-get update
RUN apt-get install openjdk-21-jdk -y

COPY . .

RUN chmod +x /gradlew
RUN ./gradlew bootJar --no-daemon

FROM openjdk:21-slim-bookworm

COPY --from=project-build /build/libs/noted-v1.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 9000

