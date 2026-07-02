# syntax=docker/dockerfile:1.7

#---------- build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp -q dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp -q -DskipTests package \
 && java -Djarmode=layertools -jar target/*.jar extract --destination target/layers

#---------- runtime stage ----------
FROM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /app
RUN addgroup -g 10001 -S app && adduser -u 10001 -S -G app app

# Layered copy: dependencies change rarely (cached), application layer changes often.
COPY --chown=app:app --from=build /app/target/layers/dependencies/ ./
COPY --chown=app:app --from=build /app/target/layers/spring-boot-loader/ ./
COPY --chown=app:app --from=build /app/target/layers/snapshot-dependencies/ ./
COPY --chown=app:app --from=build /app/target/layers/application/ ./

USER app
EXPOSE 28242

# MaxRAMPercentage lets the JVM honour the container memory limit (without it the JVM
# ignores cgroup limits and gets OOMKilled); ExitOnOutOfMemoryError makes the orchestrator
# restart on heap exhaustion instead of letting the JVM thrash.
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70 -XX:+ExitOnOutOfMemoryError"

# start-period is generous: Spring Boot 2.6 + JPA + first DB connection cold start is real.
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget -qO- http://localhost:28242/actuator/health/liveness || exit 1

# Spring Boot 2.6 loader main class. NOTE: Boot 3.2+ moved this to
# org.springframework.boot.loader.launch.JarLauncher — do not "upgrade" this path
# unless you also bump the Spring Boot parent version.
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
