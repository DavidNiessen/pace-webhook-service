package dev.niessen.webhookservice.exception.exceptions

import org.springframework.http.HttpStatus

class InvalidDeploymentTypeException(deploymentType: String?) :
    ServiceException("Invalid deployment type provided: $deploymentType", HttpStatus.INTERNAL_SERVER_ERROR)
