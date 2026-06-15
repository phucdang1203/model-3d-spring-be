# Model 3D Spring Backend

Spring Boot rewrite of the `control3D-main` backend API.

## Run

```bash
gradle bootRun
```

The default database is:

```text
jdbc:mariadb://127.0.0.1:3306/model_3d
username: root
password: admin
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

Swagger UI is available at `/swagger-ui.html`.
