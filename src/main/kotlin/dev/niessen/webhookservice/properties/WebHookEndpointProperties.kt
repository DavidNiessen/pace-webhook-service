package dev.niessen.webhookservice.properties

import dev.niessen.webhookservice.webhook.WebHookType
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "webhook.endpoint")
data class WebHookEndpointProperties(
    private val urls: List<String>,
    private val types: List<WebHookType>,
    var port: Int,
    val timeoutMs: Long,
) {

    val urlWebHookMap: Map<String, WebHookType> = urls.zip(types).toMap()
    val typeGroupMap: Map<WebHookType, List<String>> =
        types.zip(urls)
            .groupBy({ it.first }, { it.second })
    val typeList = typeGroupMap.keys

    fun getUrlsForType(type: WebHookType): List<String> =
        typeGroupMap[type] ?: emptyList()

}
