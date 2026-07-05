# bipApi

REST API для управления учебной нагрузкой — преподаватели, предметы, группы и записи
нагрузки — с импортом/экспортом XLSX через Apache POI. Учебный проект (практика).

> Этот код был написан Энринко (Максим Корниенко), студентом, изучающим Java.

> **Язык**: Русский · [English](README.md)

## Структура репозитория

- `MyPractice/` — сервер на Spring Boot (этот репозиторий).
- `client/` — десктопный JavaFX-клиент, подключён как **git-сабмодуль**
  ([Enrinko/bip-client](https://github.com/Enrinko/bip-client)). Клонируйте с
  `git clone --recurse-submodules …` или выполните `git submodule update --init` после клонирования.

## Стек

| Слой | Технология |
| --- | --- |
| Язык | Java 17 (LTS) |
| Фреймворк | Spring Boot 2.6.7 |
| Хранилище | MySQL 8.4 (JPA / Hibernate) |
| Очередь | нет |
| Кэш | нет |
| Контейнеризация | Docker / Temurin 17 JRE (Alpine) |
| Оркестрация | docker-compose |

## Быстрый старт (локальная разработка)

```bash
# 1. Скопировать шаблон env и заполнить секреты (минимум MYSQL_PASSWORD + MYSQL_ROOT_PASSWORD)
cp .env.example .env

# 2. Запустить стек (app + MySQL); ждём, пока станет healthy
docker compose up --wait

# 3. Проверить здоровье
curl http://localhost:28242/actuator/health
```

API слушает на `localhost:28242`. Базовый путь — `/bipapi` (например, `GET /bipapi/loads/all`);
контроллеры лежат в `practice.server.practiceServer.controller`. Данные MySQL хранятся в
именованном томе `mysql_data`.

### Опциональные dev-сервисы

- **Adminer** (браузер БД) — `http://localhost:8081` (сервер: `mysql`), поднимается из
  `compose.override.yaml`, который в dev загружается автоматически.
- **Observability** (Prometheus + Grafana) — включается профилем:

  ```bash
  docker compose --profile observability up --wait
  ```

  - Prometheus: `http://localhost:9090` (скрейпит `app:28242/actuator/prometheus`)
  - Grafana: `http://localhost:3000` (вход по `GRAFANA_ADMIN_USER` / `GRAFANA_ADMIN_PASSWORD`
    из `.env`; дашборд «bipApi — Service Overview» провижнится автоматически)

## Развёртывание

```bash
# Прод-режим (валидация схемы вместо авто-обновления, логи выключены):
docker compose -f compose.yaml -f compose.prod.yaml up -d
```

Для реального прода направьте приложение на управляемый MySQL (RDS / Cloud SQL) и уберите
сервис `mysql` из прод-оверлея. См. [SECURITY.ru.md](SECURITY.ru.md).

## Конфигурация

Вся конфигурация — через переменные окружения. Полный список — в `.env.example`.

| Переменная | Обязательная | Комментарий |
| --- | --- | --- |
| `MYSQL_PASSWORD` | ✅ | Пароль пользователя БД. Без него приложение и БД падают сразу. |
| `MYSQL_ROOT_PASSWORD` | ✅ | Root-пароль контейнера MySQL. |
| `GRAFANA_ADMIN_PASSWORD` | ✅ (только observability) | Пароль admin в Grafana. Без дефолта. |
| `APP_PORT` | — | Хост-порт API (по умолчанию `28242`). |
| `MYSQL_DATABASE` / `MYSQL_USER` | — | По умолчанию `bipLoad` / `bip`. |

Приложение читает `MYSQL_*` и `SERVER_PORT` через плейсхолдеры Spring в
`src/main/resources/application.properties`. Credentials **не** захардкожены.

## Безопасность

Полный контракт, overrides и процедура disclosure: [SECURITY.ru.md](SECURITY.ru.md). Ключевое:

- Контейнер под non-root (UID 10001), read-only root FS, все caps сброшены, `no-new-privileges`.
- Базовые образы запиннены по тегу (`eclipse-temurin:17-jre-alpine`, `mysql:8.4`) — без `:latest`.
- Секреты не коммитятся: `.env` в gitignore, `.env.example` с плейсхолдерами закоммичен.
- CI: поиск секретов (gitleaks), скан зависимостей (Trivy), линт Dockerfile (hadolint).

## Контрибьюшен

Правила веток, формат коммитов и процесс PR — в [CONTRIBUTING.md](CONTRIBUTING.md).

## Лицензия

Распространяется под лицензией [MIT](LICENSE).
