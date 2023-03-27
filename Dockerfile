FROM amazoncorretto:17 AS wrapper

WORKDIR /usr/src/app
COPY gradle gradle
COPY gradlew gradlew
RUN ./gradlew
COPY . .
RUN ./gradlew -i bootJar

FROM amazoncorretto:17
WORKDIR /usr/src/app
COPY --from=wrapper /usr/src/app/build/libs/leaderboard-example-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]