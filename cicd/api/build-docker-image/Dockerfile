ARG JAVA_VERSION
FROM eclipse-temurin:${JAVA_VERSION}

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]