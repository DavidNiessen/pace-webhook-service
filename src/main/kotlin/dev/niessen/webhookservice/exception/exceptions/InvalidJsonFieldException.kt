package dev.niessen.webhookservice.exception.exceptions

import org.springframework.http.HttpStatus

class InvalidJsonFieldException(fieldName: String) : ServiceException(fieldName, HttpStatus.INTERNAL_SERVER_ERROR)
