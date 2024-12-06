FROM maven:3.9.8-eclipse-temurin-21 as build

WORKDIR /build

COPY admin-service/pom.xml ./pom.xml
COPY admin-service/src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /build/target/admin-service-0.0.1-SNAPSHOT.jar admin-service.jar

ENTRYPOINT ["java", "-jar", "admin-service.jar"]

EXPOSE 8087