#FROM ubuntu:latest AS build
#
#RUN apt-get update
#RUN apt-get install openjdk-17-jdk -y
#COPY . .
#
#RUN apt-get install maven -y
#RUN mvn clean install
#
#FROM openjdk:17-jdk-slim
#
#EXPOSE 8080
#
#COPY --from=build target/DailyAccountingServerSide-0.0.1.jar app.jar
#
#ENTRYPOINT [ "java", "-jar", "app.jar" ]
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080