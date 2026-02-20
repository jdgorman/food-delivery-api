# Use Amazon Corretto (AWS's OpenJDK distribution) as base image
FROM amazoncorretto:21-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file from target directory
COPY target/*.jar app.jar

# Expose port 5000
EXPOSE 5000

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=aws", "-jar", "app.jar"]