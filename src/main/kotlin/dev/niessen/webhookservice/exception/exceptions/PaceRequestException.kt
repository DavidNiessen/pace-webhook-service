package dev.niessen.webhookservice.exception.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class PaceRequestException(message: String, statusCode: HttpStatusCode) :
    ServiceException("Pace request failed: statusCode=${statusCode.value()} message=$message", HttpStatus.INTERNAL_SERVER_ERROR)
