package dev.niessen.webhookservice.repository

import com.fasterxml.jackson.databind.JsonNode
import dev.niessen.webhookservice.converter.StringToJsonNodeConverter
import dev.niessen.webhookservice.exception.exceptions.PaceRequestException
import dev.niessen.webhookservice.properties.PaceProperties
import dev.niessen.webhookservice.utils.URLHelper
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.http.client.ClientHttpResponse
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Service
class PaceRepository(
    internal val properties: PaceProperties,
    private val jsonConverter: StringToJsonNodeConverter,
    private val restTemplateBuilder: RestTemplateBuilder,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private lateinit var httpClient: RestTemplate
    private lateinit var headers: HttpHeaders

    private val errHandler = object : DefaultResponseErrorHandler() {
        override fun hasError(response: ClientHttpResponse) = false
    }

    @PostConstruct
    fun init() {
        httpClient = restTemplateBuilder
            .readTimeout(Duration.ofMillis(properties.timeoutMs))
            .connectTimeout(Duration.ofMillis(properties.timeoutMs))
            .build()
            .apply {
                errorHandler = errHandler
            }

        headers = HttpHeaders().apply {
            accept = listOf(MediaType.APPLICATION_JSON)
            set("User-Agent", properties.userAgent)
            set("Apikey", properties.apiKey)
        }

    }

    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 100, multiplier = 2.0))
    fun fetchPaceJson(): JsonNode {
        val paceUrl = URLHelper.constructPaceUrl(properties.host, properties.path, properties.port, properties.https)
        val response = httpClient.exchange(
            paceUrl,
            HttpMethod.GET,
            HttpEntity<Any>(headers),
            String::class.java)

        logger.debug("fetchPaceJson called: url=$paceUrl, statusCode=${response.statusCode} body=${response.body}")

        if (!response.statusCode.is2xxSuccessful) {
            throw PaceRequestException(
                "pace request failed with code: ${response.statusCode.value()}",
                HttpStatus.INTERNAL_SERVER_ERROR,
            )
        }

        val responseBody = response.body

        if (responseBody.isNullOrBlank()) {
            throw PaceRequestException(
                "pace request failed: invalid response body received",
                response.statusCode
            )
        }

        return jsonConverter.convert(responseBody, false)
    }

}
