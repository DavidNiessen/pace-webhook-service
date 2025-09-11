package dev.niessen.webhookservice.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "pace")
data class PaceProperties(
    val host: String,
    val path: String,
    var port: Int,
    var apiKey: String,
    val timeoutMs: Long,
    val userAgent: String,
    val https: Boolean
)
