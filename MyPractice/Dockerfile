# syntax=docker/dockerfile:1.7

#---------- build stage ----------
FROM maven:3-eclipse-temurin-26 AS build
WORKDIR /app
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp -q dependency:go-offline
COPY src ./src
# Boot 4 layered image. Boot 3.2+ replaced the old "layertools" jarmode with "tools":
# extract --layers reorganises the fat jar into cache-friendly layer directories, and
# rewrites the app jar to reference the sibling dependency layers. Copy the built jar to a
# stable name first so the runtime ENTRYPOINT doesn't depend on the version-stamped name.
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp -q -DskipTests package \
 && cp target/*.jar target/app.jar \
 && java -Djarmode=tools -jar target/app.jar extract --layers --destination target/extracted

#---------- runtime stage ----------
FROM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /app
RUN addgroup -g 10001 -S app && adduser -u 10001 -S -G app app

# Layered copy: dependencies change rarely (cached), application layer changes often.
COPY --chown=app:app --from=build /app/target/extracted/dependencies/ ./
COPY --chown=app:app --from=build /app/target/extracted/spring-boot-loader/ ./
COPY --chown=app:app --from=build /app/target/extracted/snapshot-dependencies/ ./
COPY --chown=app:app --from=build /app/target/extracted/application/ ./

USER app
EXPOSE 28242

# MaxRAMPercentage lets the JVM honour the container memory limit (without it the JVM
# ignores cgroup limits and gets OOMKilled); ExitOnOutOfMemoryError makes the orchestrator
# restart on heap exhaustion instead of letting the JVM thrash.
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70 -XX:+ExitOnOutOfMemoryError"

# start-period is generous: Spring Boot + JPA + first DB connection cold start is real.
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget -qO- http://localhost:28242/actuator/health/liveness || exit 1

# Boot 4 extracted-jar launch: "tools" extraction rewrote app.jar to load the sibling
# dependency layers, so a plain "java -jar" is the documented entrypoint (the old
# org.springframework.boot.loader.JarLauncher path is gone in Boot 4).
ENTRYPOINT ["java", "-jar", "app.jar"]
