FROM maven:3.9.8-eclipse-temurin-21 as build

WORKDIR /build

COPY gateway-server/pom.xml ./pom.xml
COPY gateway-server/src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /build/target/gateway-server-0.0.1-SNAPSHOT.jar gateway-server.jar

ENTRYPOINT ["java", "-jar", "gateway-server.jar"]

EXPOSE 8088