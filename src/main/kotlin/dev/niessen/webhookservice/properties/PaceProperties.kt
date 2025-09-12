package dev.niessen.webhookservice.properties

import dev.niessen.webhookservice.model.MenuRestaurant
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "pace")
data class PaceProperties(
    val host: String,
    val path: String,
    var port: Int,
    var apiKey: String,
    val timeoutMs: Long,
    val userAgent: String,
    val https: Boolean,
    val cacheTtlMs: Long,
    val restaurantWhitelist: Set<MenuRestaurant>,
    val mealtimeWhitelist: Set<String>,
    val menuLabelBlacklist: Set<String>
)
