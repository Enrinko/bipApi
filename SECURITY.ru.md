# Безопасность

> **Язык**: Русский · [English](SECURITY.md)

Контракт безопасности **bipApi**: что enforced, что переопределено и почему, как сообщить об
уязвимости. Цель развёртывания — **docker-compose** (без Kubernetes), поэтому K8s-специфичные
пункты помечены N/A и заменены харденингом на уровне compose.

## Как сообщить об уязвимости

**Не** открывайте публичный issue. Пишите на **enrinkopiece@gmail.com**: описание, шаги
воспроизведения, затронутый коммит. Дайте разумное окно на фикс до публичного раскрытия.

## Модель угроз (в скоупе)

- Компрометация работающего контейнера (RCE, CVE в зависимости).
- Утечка credentials через логи, env или систему контроля версий.
- Supply-chain атака на зависимость или базовый образ.
- Escape привилегий из контейнера на хост.
- Несанкционированный доступ к данным БД / приложения.

Не в скоупе: DDoS (уровень edge), социальная инженерия, физический доступ к хостам.

## Enforced baseline

| # | Контроль | Статус | Где |
| --- | --- | --- | --- |
| 1 | Контейнер под non-root (UID 10001) | ✅ | `MyPractice/Dockerfile` |
| 2 | Базовые образы по тегу (без `:latest`) | ✅ | `MyPractice/Dockerfile`, `compose.yaml` |
| 3 | Версии зависимостей запиннены (BOM Spring Boot parent) | ✅ | `MyPractice/pom.xml` |
| 4 | Секреты не в git; `.env` игнорится, `.env.example` закоммичен | ✅ | `.gitignore`, `.dockerignore`, `.env.example` |
| 5 | Healthcheck (`/actuator/health/liveness`) | ✅ | Actuator + `HEALTHCHECK` в `Dockerfile` |
| 6 | Read-only root FS | ✅ (compose) | `compose.yaml` (`read_only` + tmpfs `/tmp`) |
| 7 | Все Linux capabilities сброшены | ✅ (compose) | `compose.yaml` (`cap_drop: [ALL]`) |
| 8 | `no-new-privileges` | ✅ (compose) | `compose.yaml` (`security_opt`) |
| 9 | Лимиты ресурсов | ✅ (compose) | `compose.yaml` (`mem_limit`, `cpus`) |
| 10 | `seccompProfile: RuntimeDefault` | N/A | нет цели Kubernetes |
| 11 | Default-deny `NetworkPolicy` | N/A | нет цели Kubernetes |
| 12 | CI скан зависимостей (Trivy) | ⚠️ report-only | `.github/workflows/ci.yml` — см. Overrides |
| 13 | CI поиск секретов (gitleaks) | ✅ | `.github/workflows/ci.yml` |
| 14 | Линт Dockerfile (hadolint) | ✅ | `.github/workflows/ci.yml` |

## Overrides (заявленные проектом)

| Пункт | Override | Причина | Рекомендация |
| --- | --- | --- | --- |
| Trivy (#12) | `exit-code: 0` (только отчёт) | Теперь поддерживаемая линейка **Spring Boot 4.x**, и CVE фреймворка, из-за которых был этот override, устранены; оставлено в режиме отчёта, пока прогон Trivy не подтвердит отсутствие неустранимых CRITICAL/HIGH (например, Apache POI). | Убедиться, что скан чистый, затем `exit-code: 1` для гейта билда. |
| Схема БД | `ddl-auto=update` в dev | Приложение полагается на авто-создание схемы Hibernate. | `compose.prod.yaml` ставит `validate`; внедрить Flyway. |
| Actuator | `/actuator/prometheus` без auth | Только локальная observability. | Ограничить сетью / добавить auth до любого не-локального доступа. |
| Пиннинг Actions | По тегам, не по commit SHA | Читаемость; SHA сложно проверить вручную. | Пиннить по SHA (Dependabot уже включён и обновляет их). |

## Рекомендуется (пока не сделано)

- [x] Мигрировано: Spring Boot 2.6.7 → **4.0.7** и Java 17 → **25 LTS** (убирает находки Trivy по EOL-фреймворку)
- [ ] Flyway-миграции; `ddl-auto=validate` везде
- [ ] TLS с проверкой сертификата сервера БД (`sslMode=VERIFY_IDENTITY`)
- [ ] SAST в CI (Semgrep, `p/owasp-top-ten p/java`)
- [ ] Подпись образов (cosign) + SBOM (syft) после публикации образов
- [x] Dependabot включён (`.github/dependabot.yml`)

## Обращение с секретами

| Слой | Механизм |
| --- | --- |
| Локальная разработка | `.env` (gitignored), авто-загрузка docker-compose |
| CI | GitHub Actions Secrets (`${{ secrets.* }}`) |
| Контейнерный рантайм | Переменные окружения из compose |

Никогда не коммитьте заполненный `.env`, не вставляйте секреты в PR, не логируйте, не
передавайте через `--build-arg`. Ранее закоммиченный пароль БД (`application.properties`)
вынесен в `${MYSQL_PASSWORD}`. **Смените этот пароль** — он был в истории git и считается
скомпрометированным.

## Логирование

`spring.jpa.show-sql` включён в dev и выключен в прод-оверлее. Не логируйте тела запросов с
данными загруженных таблиц или credentials.
