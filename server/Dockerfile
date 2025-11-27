FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
RUN mkdir -p /app/data
RUN mkdir -p /app/files
COPY target/csp-0.0.1-SNAPSHOT.jar server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "server.jar"]
