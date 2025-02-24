# Build stage
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /build
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# Runtime stage
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]