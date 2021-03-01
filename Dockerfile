FROM openjdk:11-jdk-stretch

RUN mkdir /app
WORKDIR /app
COPY ./gradlew /app
COPY ./build.gradle /app
COPY ./settings.gradle /app
COPY ./src /app/src
COPY ./gradle /app/gradle
RUN ./gradlew build
# ENTRYPOINT ["sh", "./gradlew", "bootRun"]
ENTRYPOINT ["java","-Dspring.main.banner-mode=off","-jar","/app/build/libs/poc-spring-app-0.0.1-SNAPSHOT.jar"]