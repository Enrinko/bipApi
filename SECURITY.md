# Security

> **Language**: English · [Русский](SECURITY.ru.md)

Security posture of **bipApi** — what's enforced, what was overridden and why, and how to
report a vulnerability. Deploy target is **docker-compose** (no Kubernetes), so K8s-specific
baseline items are marked N/A and replaced with compose-level hardening.

## Reporting a vulnerability

Do **not** open a public issue for security problems. Email **enrinkopiece@gmail.com** with a
description, reproduction steps, and affected commit. Please allow a reasonable fix window
before public disclosure.

## Threat model (in scope)

- Compromise of a running container (RCE in the app, dependency CVE).
- Leak of credentials via logs, env, or source control.
- Supply-chain attack on a dependency or base image.
- Privilege escalation from the container to the host.
- Unauthorized DB / application-data access.

Out of scope: DDoS (edge concern), social engineering, physical host access.

## Enforced baseline

| # | Control | Status | Where |
| --- | --- | --- | --- |
| 1 | Container runs as non-root (UID 10001) | ✅ | `MyPractice/Dockerfile` |
| 2 | Base images pinned by tag (no `:latest`) | ✅ | `MyPractice/Dockerfile`, `compose.yaml` |
| 3 | Dependency versions pinned (Spring Boot parent BOM) | ✅ | `MyPractice/pom.xml` |
| 4 | Secrets not committed; `.env` ignored, `.env.example` committed | ✅ | `.gitignore`, `.dockerignore`, `.env.example` |
| 5 | Healthcheck endpoint (`/actuator/health/liveness`) | ✅ | app (Actuator) + `Dockerfile HEALTHCHECK` |
| 6 | Read-only root FS | ✅ (compose) | `compose.yaml` (`read_only: true` + tmpfs `/tmp`) |
| 7 | All Linux capabilities dropped | ✅ (compose) | `compose.yaml` (`cap_drop: [ALL]`) |
| 8 | `no-new-privileges` | ✅ (compose) | `compose.yaml` (`security_opt`) |
| 9 | Resource limits | ✅ (compose) | `compose.yaml` (`mem_limit`, `cpus`) |
| 10 | `seccompProfile: RuntimeDefault` | N/A | no Kubernetes target |
| 11 | Default-deny `NetworkPolicy` | N/A | no Kubernetes target |
| 12 | CI dependency vuln scan (Trivy) | ⚠️ report-only | `.github/workflows/ci.yml` — see Overrides |
| 13 | CI secret detection (gitleaks) | ✅ | `.github/workflows/ci.yml` |
| 14 | Dockerfile lint (hadolint) | ✅ | `.github/workflows/ci.yml` |

## Overrides (declared by the project)

| Item | Override | Reason | Recommended fix |
| --- | --- | --- | --- |
| Trivy (baseline #12) | `exit-code: 0` (report-only) | Now on a supported **Spring Boot 4.x** line, so the framework CVEs that forced this override are resolved; kept report-only until a Trivy run confirms no unfixable CRITICAL/HIGH remain (e.g. Apache POI). | Confirm the scan is clean, then set `exit-code: 1` to gate the build. |
| Schema management | `spring.jpa.hibernate.ddl-auto=update` in dev | App relies on Hibernate auto-creating the schema. | `compose.prod.yaml` overrides to `validate`; adopt Flyway migrations. |
| Actuator exposure | `/actuator/prometheus` unauthenticated | Local-dev observability only. | Restrict by network / add auth before any non-local exposure. |
| GitHub Actions pinning | Pinned to version tags, not commit SHA | Readability; SHAs can't be hand-verified reliably. | Pin to commit SHA (Dependabot keeps them fresh — already enabled). |

## Recommended but not implemented

- [x] Migrated Spring Boot 2.6.7 → **4.0.7** and Java 17 → **25 LTS** (removes the EOL-framework Trivy findings)
- [ ] Flyway migrations; set `ddl-auto=validate` everywhere
- [ ] TLS with verified server cert for the DB connection (`sslMode=VERIFY_IDENTITY`)
- [ ] SAST in CI (Semgrep, `p/owasp-top-ten p/java`)
- [ ] Image signing (cosign, keyless OIDC) + SBOM (syft) once images are published
- [x] Dependabot enabled (`.github/dependabot.yml`)

## Secrets handling

| Layer | Mechanism |
| --- | --- |
| Local dev | `.env` (gitignored), auto-loaded by docker-compose |
| CI | GitHub Actions Secrets (`${{ secrets.* }}`) |
| Container runtime | Environment variables injected by compose |

Never commit a populated `.env`, paste secrets in PRs, log them, or pass them as `--build-arg`.
The previously committed DB password (`application.properties`) has been externalized to
`${MYSQL_PASSWORD}`. **Rotate that password** — it existed in git history and must be
considered compromised.

## Logging

`spring.jpa.show-sql` is enabled in dev and disabled in the prod overlay. Do not log request
bodies containing uploaded spreadsheet data or credentials.
