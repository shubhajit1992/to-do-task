# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the built jar file
COPY build/libs/todotask-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Set environment variables for Spring profile and Java options (optional)
# ENV SPRING_PROFILES_ACTIVE=prod
# ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
