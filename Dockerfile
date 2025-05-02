FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY  ${JAR_FILE} app.jar
EXPOSE 2000
ENTRYPOINT ["java", "-jar", "/app.jar"]
