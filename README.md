# Model 3D Spring Backend

Spring Boot rewrite of the `control3D-main` backend API.

## Run

```bash
gradle bootRun
```

The local profile runs on:

```text
http://localhost:8778
```

The default local database is SQLite. It is created automatically at:

```text
model3d-local.sqlite
```

The schema is initialized from:

```text
src/main/resources/db/schema.sql
```

## Security Modes

`app.security.enabled=false` is the default so normal development and tests can call both Admin and Game APIs without login.

When the project is ready for production-like testing, set:

```yaml
app:
  security:
    enabled: true
```

Then Admin APIs require a Bearer token from `POST /api/v1/admin/auth/login`. Game APIs remain public, but game session start requires `playerName`.

## API Groups

- `GET /api/v1/models`: public model catalogue.
- `POST /api/v1/admin/models/upload`: upload and register a 3D model.
- `POST /api/v1/admin/animations/upload`: upload and register an FBX or ZIP animation asset.
- `GET /api/v1/element-types`: public element type catalogue.
- `GET /api/v1/game/levels`: public playable levels.
- `POST /api/v1/game/sessions/start`: start a public game session with a player name.

Swagger UI is available at `http://localhost:8778/swagger-ui.html`.
