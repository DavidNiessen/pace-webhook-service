package dev.niessen.webhookservice.utils

import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate

object TestUtils {

    private val defaultErrorHandler = object : DefaultResponseErrorHandler() {
        override fun hasError(response: ClientHttpResponse) = false
    }

    val httpClient = RestTemplate().apply {
        errorHandler = defaultErrorHandler
    }
}
