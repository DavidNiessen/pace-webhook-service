package dev.niessen.webhookservice.exception

import dev.niessen.webhookservice.exception.exceptions.ServiceException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(exception: ServiceException) =
        ProblemDetail.forStatusAndDetail(exception.statusCode, exception.message)

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFound(exception: NoResourceFoundException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Resource not found")

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupported(exception: HttpRequestMethodNotSupportedException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, exception.message)

    @ExceptionHandler
    fun handleGeneric(exception: Exception) =
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred")
}
