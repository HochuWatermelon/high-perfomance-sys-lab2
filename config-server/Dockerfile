FROM maven:3.9.8-eclipse-temurin-21 as build

WORKDIR /build

COPY config-server/pom.xml ./pom.xml
COPY config-server/src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /build/target/config-server-0.0.1-SNAPSHOT.jar config-server.jar

ENTRYPOINT ["java", "-jar", "config-server.jar"]

EXPOSE 8888