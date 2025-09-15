package dev.niessen.webhookservice.richtext

import dev.niessen.webhookservice.model.MenuModel
import dev.niessen.webhookservice.model.MenuProperty
import dev.niessen.webhookservice.model.MenuRestaurant
import dev.niessen.webhookservice.utils.IconUtil
import dev.niessen.webhookservice.utils.TimeUtils
import dev.niessen.webhookservice.webhook.WebHookType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SlackRichTextMessageBuilder(
    @Value("\${application.formatted-version}") val appVersion: String,
    val iconUtil: IconUtil,
    val timeUtils: TimeUtils,
) : RichTextMessageBuilder {

    override fun webHookType() = WebHookType.SLACK

    override fun buildMessage(menu: List<MenuModel>): String {
        val grouped = menu.groupBy { it.restaurant }
        val formattedDate = timeUtils.formatDate(timeUtils.today())

        return slackMessage {
            // -- HEADER
            header("Heutige Speisekarte ($formattedDate)")

            // -- RESTAURANT GROUPS
            MenuRestaurant.entries
                .filter { grouped.containsKey(it) }
                .forEach { restaurant ->
                    header(restaurant.value)
                    divider()

                    // -- MENU ITEMS
                    grouped[restaurant]!!.sortedByDescending { it.price }.forEach { model ->
                        sectionMrkdwn(
                            "*${model.menuName}*${
                                if (model.description.isNullOrBlank()) "" else " - ${model.description}"
                            }"
                        )

                        if (model.price != null || model.properties.isNotEmpty()) {
                            context(
                                *listOfNotNull(
                                    model.price?.let {
                                        PlainTextElement("${String.format("%.2f", it)}€".padEnd(7, ' '))
                                    }
                                ).plus(
                                    MenuProperty.entries.mapNotNull { property ->
                                        val url = iconUtil.propertyToIconUrl(property)
                                        ImageElement(
                                            url ?: "",
                                            property.value
                                        ).takeIf { property in model.properties && url != null }
                                    }
                                ).toTypedArray()
                            )
                        }
                    }
                }
            divider()

            // -- ICON EXPLANATION --
            context(
                *(MenuProperty.entries.map { property ->
                    val url = iconUtil.propertyToIconUrl(property)
                    listOfNotNull(
                        ImageElement(url ?: "", property.value),
                        PlainTextElement(property.value),
                    ).takeIf { url != null } ?: emptyList()
                }).flatten().toTypedArray(),
            )

            // -- FOOTER
            context(MrkdwnText("$appVersion - $formattedDate"))
        }
    }
}
