package dev.niessen.webhookservice.exception.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

open class ServiceException(
    override val message: String,
    val statusCode: HttpStatusCode,
) : RuntimeException(message) {

    constructor(message: String, statusCode: Int) : this(message, HttpStatusCode.valueOf(statusCode))

    constructor(message: String) : this(message, HttpStatus.INTERNAL_SERVER_ERROR.value())

}
