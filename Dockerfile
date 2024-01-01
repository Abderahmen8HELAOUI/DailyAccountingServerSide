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
#FROM eclipse-temurin:17-jdk-alpine
#VOLUME /tmp
#COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
#EXPOSE 8080

#
# Build stage
#
FROM maven:3.8.2-jdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/DailyAccountingServerSide-0.0.1-SNAPSHOT.jar DailyAccountingServerSide.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]