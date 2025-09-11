package dev.niessen.webhookservice.richtext.slack

import dev.niessen.webhookservice.model.MenuModel
import dev.niessen.webhookservice.model.MenuProperty
import dev.niessen.webhookservice.utils.IconUtil
import dev.niessen.webhookservice.utils.TimeUtils

class SlackMenuMessage {

    private val timeUtils = TimeUtils()

    fun buildSlackMenuMessage(menu: List<MenuModel>): String {
        val grouped = menu.groupBy { it.restaurant }
        val formattedDate = timeUtils.formattedDate()

        return slackMessage {
            header("Heutige Speisekarte ($formattedDate)")

            grouped.keys.forEach { group ->
                header(group.value)
                divider()

                grouped[group]!!.forEach { model ->
                    sectionMrkdwn("*${model.menuName}*${if (model.description.isNullOrBlank()) "" else ": "}${model.description}")

                    val properties = model.properties
                    if (properties.isNotEmpty()) {
                        context(
                            PlainTextElement("${model.price}"),
                            *(properties.map { property ->
                                ImageElement(IconUtil.propertyToIconUrl(property), property.value)
                            }).toTypedArray(),
                        )
                    }
                }
            }
            divider()
            context(
                *(MenuProperty.entries.map { property ->
                    listOf(
                        ImageElement(IconUtil.propertyToIconUrl(property), property.value),
                        PlainTextElement(property.value),
                    )
                }).flatten().toTypedArray(),
            )
        }
    }
}
