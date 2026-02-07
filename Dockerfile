# 1. Use an official Java runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# 2. Set the working directory inside the container
WORKDIR /app

# 3. Copy the compiled JAR file into the container
# (Make sure you run 'mvn clean install' first!)
COPY target/cab-booking-api-0.0.1-SNAPSHOT.jar app.jar

# 4. Expose the port your app runs on
EXPOSE 8080

# 5. Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]