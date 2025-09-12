package dev.niessen.webhookservice.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "icons")
data class IconProperties(
    val urls: Map<String, String>
)
