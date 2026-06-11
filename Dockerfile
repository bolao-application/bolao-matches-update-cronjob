FROM maven:3.6.3-jdk-11 AS build

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src/ src/
RUN mvn package -DskipTests

FROM eclipse-temurin:11-jre

WORKDIR /app

RUN groupadd -r app && useradd -r -g app app

COPY --from=build /app/target/bolao-matches-update-cronjob-*.jar app.jar

USER app

ENTRYPOINT ["java", "-jar", "app.jar"]
