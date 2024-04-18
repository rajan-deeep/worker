# Use an OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the compiled Java application JAR file into the container
COPY target/workerRedisQueue-1.0-SNAPSHOT.jar /app/main-server.jar

# Expose the port on which the Spring Boot application will listen
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "main-server.jar"]
