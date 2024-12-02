FROM maven:3.9.8-eclipse-temurin-21 as build

WORKDIR /build

COPY monolit-service/pom.xml ./pom.xml
COPY monolit-service/src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /build/target/monolit-service-0.0.1-SNAPSHOT.jar monolit-service.jar

ENTRYPOINT ["java", "-jar", "monolit-service.jar"]

EXPOSE 8089