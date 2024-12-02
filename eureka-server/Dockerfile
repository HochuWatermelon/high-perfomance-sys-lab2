FROM maven:3.9.8-eclipse-temurin-21 as build

WORKDIR /build

COPY eureka-server/pom.xml ./pom.xml
COPY eureka-server/src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /build/target/eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar

ENTRYPOINT ["java", "-jar", "eureka-server.jar"]

EXPOSE 8761