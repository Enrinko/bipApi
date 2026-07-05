# bipApi

REST API for managing university teaching load — teachers, subjects, groups, and load
records — with XLSX import/export via Apache POI. College practice project.

> This code was written by Enrinko (Maxim Kornienko), a student learning Java.

> **Language**: English · [Русский](README.ru.md)

## Repository layout

- `MyPractice/` — the Spring Boot server (this repo).
- `client/` — the JavaFX desktop client, included as a **git submodule**
  ([Enrinko/bip-client](https://github.com/Enrinko/bip-client)). Clone with
  `git clone --recurse-submodules …`, or run `git submodule update --init` after cloning.

## Stack

| Layer | Tech |
| --- | --- |
| Language | Java 17 (LTS) |
| Framework | Spring Boot 2.6.7 |
| Persistence | MySQL 8.4 (JPA / Hibernate) |
| Messaging | none |
| Cache | none |
| Container runtime | Docker / Temurin 17 JRE (Alpine) |
| Orchestration | docker-compose |

## Quick start (local dev)

```bash
# 1. Copy env template and fill in secrets (at minimum MYSQL_PASSWORD + MYSQL_ROOT_PASSWORD)
cp .env.example .env

# 2. Start the stack (app + MySQL); waits until healthy
docker compose up --wait

# 3. Check health
curl http://localhost:28242/actuator/health
```

The API listens on `localhost:28242`. Base path is `/bipapi` (e.g. `GET /bipapi/loads/all`);
controllers live under `practice.server.practiceServer.controller`. MySQL data persists in the
`mysql_data` named volume.

### Optional dev services

- **Adminer** (DB browser) is available on `http://localhost:8081` (server: `mysql`) once the
  stack is up — it comes from `compose.override.yaml`, which loads automatically in dev.
- **Observability** (Prometheus + Grafana) is opt-in:

  ```bash
  docker compose --profile observability up --wait
  ```

  - Prometheus: `http://localhost:9090` (scrapes `app:28242/actuator/prometheus`)
  - Grafana: `http://localhost:3000` (login with `GRAFANA_ADMIN_USER` / `GRAFANA_ADMIN_PASSWORD`
    from `.env`; the "bipApi — Service Overview" dashboard is provisioned automatically)

## Deployment

```bash
# Production-style run (validates schema instead of auto-updating, logs off):
docker compose -f compose.yaml -f compose.prod.yaml up -d
```

For real production, point the app at a managed MySQL (RDS / Cloud SQL) and remove the
`mysql` service from the prod overlay. See [SECURITY.md](SECURITY.md) for the hardening notes.

## Configuration

All runtime configuration is environment-driven. See `.env.example` for the full list.

| Variable | Required | Notes |
| --- | --- | --- |
| `MYSQL_PASSWORD` | ✅ | App DB user password. App and DB both fail fast without it. |
| `MYSQL_ROOT_PASSWORD` | ✅ | MySQL container root password. |
| `GRAFANA_ADMIN_PASSWORD` | ✅ (observability only) | Grafana admin password. No default — set it before using the profile. |
| `APP_PORT` | — | Host port for the API (default `28242`). |
| `MYSQL_DATABASE` / `MYSQL_USER` | — | Default `bipLoad` / `bip`. |

The app reads `MYSQL_*` and `SERVER_PORT` via Spring placeholders in
`src/main/resources/application.properties`. Credentials are **not** hardcoded.

## Security

Full posture, overrides, and disclosure process: [SECURITY.md](SECURITY.md). Highlights:

- Container runs as non-root (UID 10001), read-only root FS, all caps dropped, `no-new-privileges`.
- Base images pinned by tag (`eclipse-temurin:17-jre-alpine`, `mysql:8.4`) — never `:latest`.
- Secrets never committed: `.env` gitignored, `.env.example` committed with placeholders.
- CI runs secret scanning (gitleaks), dependency scanning (Trivy), and Dockerfile linting (hadolint).

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for branch naming, commit format, and PR process.

## License

Released under the [MIT License](LICENSE).
