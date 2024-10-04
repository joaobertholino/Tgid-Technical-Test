FROM maven:3.9.9-eclipse-temurin-21 AS build
COPY src tgid-technical-build/src
COPY pom.xml tgid-technical-build
WORKDIR tgid-technical-build
RUN mvn clean install

FROM openjdk:21
COPY --from=build tgid-technical-build/target/*.jar tgid-technical-image/tgid-technical-test.jar
EXPOSE 8080
WORKDIR tgid-technical-image
ENTRYPOINT ["java", "-jar", "tgid-technical-test.jar"]