# Use official OpenJDK image
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy project files
COPY . .

# Build jar
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run application
CMD ["java", "-jar", "target/daily-planner-backend-1.0.0.jar"]
