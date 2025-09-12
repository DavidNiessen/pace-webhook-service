# Pace Webhook Service

This project scrapes menu data from PACE (Axel Springer) canteen and dispatches it to one or multiple webhooks.

### Deployment

This project can be deployed as a cloud function, daemon or Rest API.

You can configure the run type via `application.properties`

```
application.deploymenttype={DEPLOYMENT_TYPE}
```

Available deployment types:

* `CLOUD_FUNCTION` Invoked via AWS' Lambda or GitHub Actions, for example
* `DAEMON` Runs in the background and schedules operations via cron job
* `REST` Runs when invoked via Rest API

By default, the project runs as a cloud function and is invoked
via a GitHub Actions workflow on a CRON schedule.

### Run the project

`gradlew run`

### Run locally

Following properties in `src/main/resources/application.properties` must be set manually
or injected via environment (recommended)

| PROPERTY                 | ENVIRONMENT VARIABLE |
|--------------------------|----------------------|
| `pace.apikey`            | `PACE_API_KEY`       |
| `webhook.endpoint.urls`  | `WEBHOOK_URLS`       |
| `webhook.endpoint.types` | `WEBHOOK_TYPES`      |

`gradlew bootRun`

### Run tests

`gradlew test`

### REST Endpoints

For the REST endpoints to be enabled, please make sure that the deployment type is set to `REST`

Endpoints:

Fetch menu and dispatch via WebHooks
> GET http://localhost:8080/api/v1/dispatch

Health check
> GET http://Localhost:8080/actuator/health
