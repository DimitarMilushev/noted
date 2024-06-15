# Compile jar file
FROM ubuntu:latest AS project-build
RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
COPY . .
RUN chmod +x /gradlew
RUN ./gradlew bootJar --no-daemon

FROM openjdk:21-slim-bookworm
EXPOSE 9000/tcp
COPY --from=project-build /build/libs/noted-v1.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

