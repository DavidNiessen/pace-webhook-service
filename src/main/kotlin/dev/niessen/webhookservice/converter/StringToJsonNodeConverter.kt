package dev.niessen.webhookservice.converter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import dev.niessen.webhookservice.exception.exceptions.JsonParseException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class StringToJsonNodeConverter(
    @Qualifier("jsonObjectMapper") private val objectMapper: ObjectMapper,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun convert(source: String, isUserJson: Boolean = false): JsonNode =
        runCatching { objectMapper.readTree(source) }.getOrElse { ex ->
            logger.warn("Failed to parse json. json=${source} message=${ex.message}")
            throw JsonParseException(source, isUserJson)
        }
}
