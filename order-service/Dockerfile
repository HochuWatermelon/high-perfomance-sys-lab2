FROM maven:3.9.8-eclipse-temurin-21 as build

WORKDIR /build

COPY order-service/pom.xml ./pom.xml
COPY order-service/src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /build/target/order-service-0.0.1-SNAPSHOT.jar order-service.jar

ENTRYPOINT ["java", "-jar", "order-service.jar"]

EXPOSE 8089