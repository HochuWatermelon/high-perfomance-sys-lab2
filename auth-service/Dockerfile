FROM maven:3.9.8-eclipse-temurin-21 as build

WORKDIR /build

COPY auth-service/pom.xml ./pom.xml
COPY auth-service/src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /build/target/auth-service-0.0.1-SNAPSHOT.jar auth-service.jar

ENTRYPOINT ["java", "-jar", "auth-service.jar"]

EXPOSE 8086