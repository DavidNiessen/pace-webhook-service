package dev.niessen.webhookservice.service

import dev.niessen.webhookservice.converter.PaceJsonToModelConverter
import dev.niessen.webhookservice.properties.PaceProperties
import dev.niessen.webhookservice.properties.WebHookEndpointProperties
import dev.niessen.webhookservice.repository.PaceRepository
import dev.niessen.webhookservice.richtext.RichTextMessageBuilder
import dev.niessen.webhookservice.webhook.WebHookDispatcher
import org.springframework.stereotype.Service

@Service
class PaceService(
    private val paceRepository: PaceRepository,
    private val paceProperties: PaceProperties,
    private val paceJsonToModelConverter: PaceJsonToModelConverter,
    private val messageBuilders: MutableList<out RichTextMessageBuilder>,
    private val webHookEndpointProperties: WebHookEndpointProperties,
    private val dispatcher: WebHookDispatcher,
) {

    fun getAndDispatchTodaysMenu() {
        val paceJson = paceRepository.fetchPaceJson()
        val menuItems = paceJsonToModelConverter.convert(paceJson)
            .filter { it.restaurant in paceProperties.restaurantWhitelist }
        val webHookTypes = webHookEndpointProperties.typeGroupMap.keys

        webHookTypes.forEach { webHookType ->
            messageBuilders.find { it.webHookType() == webHookType }?.let { messageBuilder ->
                webHookEndpointProperties.getUrlsForType(webHookType).forEach { url ->
                    dispatcher.dispatchWebHook(url, messageBuilder.buildMessage(menuItems))
                }
            }
        }
    }

}
