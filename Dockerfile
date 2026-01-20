# =============================================================================
# LOGA Backend - Multi-stage Docker Build
# Spring Boot 4.0.1 + Java 25
# =============================================================================

# ==========================
# Build Stage
# ==========================
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app

# Copy gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x ./gradlew

# Download dependencies (cached layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src src

# Build application (skip tests for faster build)
RUN ./gradlew bootJar --no-daemon -x test

# ==========================
# Runtime Stage
# ==========================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Install curl for health check
RUN apk add --no-cache curl

# Create non-root user for security
RUN addgroup -S loga && adduser -S -G loga loga

# Copy jar from builder
COPY --from=builder /app/build/libs/*.jar app.jar

# Create logs directory
RUN mkdir -p /app/logs && chown -R loga:loga /app

USER loga

# Environment
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV SPRING_PROFILES_ACTIVE=prod

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
