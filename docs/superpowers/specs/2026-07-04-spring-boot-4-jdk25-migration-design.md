# Spring Boot 2.6.7 → 4.0.7, Java 17 → 25 (server)

Date: 2026-07-04 · Scope: `MyPractice/` (server only; the JavaFX client is a separate repo)

## Goal

CI (and the app) must build and run on **JDK 25**. Spring Boot 2.6.7 cannot: its bundled
ASM caps at Java 18 bytecode (major 62), so CGLIB proxying of `@Configuration` classes
throws `Unsupported class file major version 69` at context startup on Java 25. Official
Java 25 support starts at **Spring Boot 4.0** (Spring Framework 7, Java 17 baseline).

## Decision

Target **Spring Boot 4.0.7** + **Java 25**. Boot 4.0 (not 3.5) because 3.5 only supports
Java 25 on a best-effort basis. For this app the migration surface is the same as a 2→3
jump (javax→jakarta) — no Spring Security / servlet-filter / XML config to port.

## Change inventory

| File(s) | Change |
| --- | --- |
| `pom.xml` parent | `2.6.7` → `4.0.7` |
| `pom.xml` `<java.version>` | `17` → `25`; drop redundant `maven.compiler.source/target` (parent derives `--release` from `java.version`) |
| `pom.xml` MySQL driver | `mysql:mysql-connector-java` → `com.mysql:mysql-connector-j` (old GAV is unmanaged/deprecated in Boot 4) |
| `pom.xml` validation | drop the pinned `jakarta.validation-api:2.0.2`; keep it **API-only** (version now managed by Boot 4 → Jakarta Validation 3.x, `jakarta.*` namespace). **Do not** add `spring-boot-starter-validation` — see note below |
| `pom.xml` build | add `maven-compiler-plugin` with Lombok on `annotationProcessorPaths` — **required**: JDK 23+ no longer runs classpath annotation processors implicitly, so without this Lombok generates nothing and compilation fails (found during verification) |
| 4 entities + 4 exceptions | `javax.persistence.*`→`jakarta.persistence.*`, `javax.validation.*`→`jakarta.validation.*` |
| `application.properties` | no change — no explicit Hibernate dialect (auto-detected); `ddl-auto=update` and actuator keys carry over |
| `.mvn/wrapper/maven-wrapper.properties` | Maven `3.8.4` → `3.9.9` (Boot 4 + Java 25) |
| `Dockerfile` | base images `temurin-17` → `temurin-25`; `-Djarmode=layertools extract` → `-Djarmode=tools ... extract --layers` (Boot 4 removed `layertools`); entrypoint → `java -jar app.jar` |
| `.github/workflows/ci.yml` | `setup-java` `17` → `25`, job name, trivy comment; also the pre-existing `trivy-action@0.24.0` → `@v0.24.0` fix (Aqua retagged with a `v` prefix) |

## Notes / deliberately out of scope

- **API-only validation is intentional.** The project currently has no Bean Validation
  *provider* on the classpath, so the `@NotBlank`/`@NotNull` annotations are inert. Adding
  `spring-boot-starter-validation` would activate Hibernate Validator, which JPA then runs
  on persist/update — and `LoadEntity` has **`@NotBlank` on `Integer` fields**
  (`@NotBlank` is String-only), which would then throw `UnexpectedTypeException`. Keeping
  it API-only preserves current behavior. The `@NotBlank`-on-`Integer` is a pre-existing
  latent bug, left as-is.
- **POI 5.2.0 kept** (Spring Boot doesn't manage POI). Not exercised by the context-load
  test; XLSX export is a manual runtime check on Java 25.
- **Jackson** touchpoint is only `@JsonIgnore` from `com.fasterxml.jackson.annotation`,
  which is stable across Jackson 2/3 — no change needed.

## Verification

Host has JDK 25 (`25+37-LTS`) + Docker 29.6.1.
1. **PASS** — `./mvnw -B -ntp verify` on Java 25 against a `mysql:8.4` container:
   `Started PracticeServerApplicationTests in 9.6s`, `Tests run: 1, Failures: 0, Errors: 0`,
   `BUILD SUCCESS`. (First attempt failed at compile — Lombok didn't run; fixed with the
   compiler-plugin `annotationProcessorPaths` above, then passed.)
2. **PASS** — `docker compose build app` (Boot 4 image on `temurin-25`, `tools` jarmode
   extraction) + `up --wait` → `/actuator/health` = `{"status":"UP"}`, app `(healthy)`
   against MySQL 8.4. Container launches `app.jar` on Java 25.0.3 via the extracted layers.
   (A first `up` hit `Access denied` from a stale `mysql_data` volume left by an earlier run
   — a test-env artifact, not a config bug; `down -v` + fresh `up` resolved it.)

## Rollback

All changes are on the working tree / a feature branch; revert the branch to return to the
Boot 2.6.7 / Java 17 baseline. No data migration involved (`ddl-auto=update`, same schema).
