# Pace Webhook Service

This project scrapes menu data from PACE (Axel Springer) canteen and dispatches it to one or multiple webhooks.

# WORK IN PROGRESS

### Deployment

This project can be deployed as a cloud function, daemon or Rest API.

You can configure the run type via `application.properties`
```
application.deploymenttype={DEPLOYMENT_TYPE}
```
Available deployment types:
* `CLOUD_FUNCTION` -> Invoked via AWS' Lambda or GitHub Actions, for example
* `DAEMON` -> Runs in the background and schedules operations via cron job
* `REST` -> Runs when invoked via Rest API

### Run the project
`gradlew run`

### Build locally
`gradlew build`

### Test project
`gradlew test`
