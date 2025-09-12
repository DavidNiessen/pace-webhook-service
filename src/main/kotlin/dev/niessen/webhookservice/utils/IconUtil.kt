package dev.niessen.webhookservice.utils

import dev.niessen.webhookservice.model.MenuProperty
import dev.niessen.webhookservice.properties.IconProperties
import org.springframework.stereotype.Component

@Component
class IconUtil(
    private val properties: IconProperties
) {

    fun propertyToIconUrl(property: MenuProperty): String? =
        properties.urls[property.propertyKey]
}
