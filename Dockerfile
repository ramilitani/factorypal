#SpringBoot JAR
FROM openjdk:8-jdk-alpine
LABEL MAINTAINER "ramilitani@gmail.com"
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
