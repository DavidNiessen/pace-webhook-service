package dev.niessen.webhookservice.webhook

import dev.niessen.webhookservice.exception.exceptions.RequestException
import dev.niessen.webhookservice.properties.WebHookEndpointProperties
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration

@Component
class WebHookDispatcher(
    private val restTemplateBuilder: RestTemplateBuilder,
    private val defaultResponseErrorHandler: DefaultResponseErrorHandler,
    internal val properties: WebHookEndpointProperties,
) {

    private val logger = LoggerFactory.getLogger(WebHookDispatcher::class.java)

    private lateinit var httpClient: RestTemplate

    @PostConstruct
    fun init() {
        httpClient = restTemplateBuilder
            .readTimeout(Duration.ofMillis(properties.timeoutMs))
            .connectTimeout(Duration.ofMillis(properties.timeoutMs))
            .build()
            .apply {
                this.errorHandler = defaultResponseErrorHandler
            }
    }

    fun dispatchWebHook(url: String, payload: String, additionalHeaders: MultiValueMap<String, String>? = null) {
        val uri = UriComponentsBuilder.fromUriString(url).port(properties.port).build().toUri()
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        additionalHeaders?.let { headers.addAll(it) }

        val response = httpClient.exchange(
            uri,
            HttpMethod.POST,
            HttpEntity<String>(payload, headers),
            String::class.java)

        logger.debug("dispatchWebHook called: url=$uri, statusCode=${response.statusCode} requestBody=${payload} responseBody=${response.body}")

        if (!response.statusCode.is2xxSuccessful) {
            throw RequestException("webhook request failed", response.statusCode)
        }
    }
}
