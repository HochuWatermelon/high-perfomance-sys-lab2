FROM maven:3.9.8-eclipse-temurin-21 as build

WORKDIR /build

COPY worker-service/pom.xml ./pom.xml
COPY worker-service/src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /build/target/worker-service-0.0.1-SNAPSHOT.jar worker-service.jar

ENTRYPOINT ["java", "-jar", "worker-service.jar"]

EXPOSE 8085