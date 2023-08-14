ARG JAVA_VERSION
FROM eclipse-temurin:${JAVA_VERSION}

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
