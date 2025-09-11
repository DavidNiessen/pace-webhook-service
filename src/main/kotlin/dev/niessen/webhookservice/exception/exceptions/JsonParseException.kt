package dev.niessen.webhookservice.exception.exceptions

import org.springframework.http.HttpStatus

/**
 * @param providedJson the raw json string
 * @param isUserJson decides which status code shall be returned
 */
class JsonParseException(providedJson: String, isUserJson: Boolean) :
    ServiceException(
        "Failed to parse json${if (isUserJson) ": json=${providedJson}" else "." }",
        if (isUserJson) HttpStatus.BAD_REQUEST else HttpStatus.INTERNAL_SERVER_ERROR
    ) {
}
