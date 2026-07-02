# Contributing to bipApi

## Local setup

```bash
cp .env.example .env          # fill MYSQL_PASSWORD + MYSQL_ROOT_PASSWORD
docker compose up --wait      # app + MySQL

# Or run the server directly (needs a reachable MySQL + the MYSQL_* env vars):
cd MyPractice
./mvnw spring-boot:run
```

Build & test the server module:

```bash
cd MyPractice
./mvnw -B -ntp verify
```

## Branch naming

`<type>/<short-slug>` where `<type>` ∈ `feat` `fix` `refactor` `perf` `docs` `chore` `test`
`security`. Example: `feat/load-export-endpoint`, `fix/subject-null-in-load`.

## Commit messages

[Conventional Commits](https://www.conventionalcommits.org/):
`<type>(<scope>): <imperative summary ≤72 chars>`, optional body explaining **why**.

## Before opening a PR

- [ ] `./mvnw verify` passes
- [ ] No secrets committed (the CI `gitleaks` job will block them anyway)
- [ ] `.env.example` updated if you added a config variable
- [ ] `SECURITY.md` updated if the security posture changed
- [ ] New behavior has a test

## Security

Report vulnerabilities privately per [SECURITY.md](SECURITY.md) — never as a public issue.
Never commit real credentials; the DB password lives only in your local `.env`.
