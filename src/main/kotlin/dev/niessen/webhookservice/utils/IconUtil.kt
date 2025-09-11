package dev.niessen.webhookservice.utils

import dev.niessen.webhookservice.model.MenuProperty
import dev.niessen.webhookservice.model.MenuProperty.*

object IconUtil {

    // TODO replace hardcoded urls
    fun propertyToIconUrl(property: MenuProperty): String = when (property) {
        VEGAN -> "https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegan.png?raw=true"
        VEGETARIAN -> "https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true"
        PORK -> "https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/pork.png?raw=true"
        NO_SUGAR -> "https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/sugar_free.png?raw=true"
    }
}
