package dev.niessen.webhookservice.converter

import com.fasterxml.jackson.databind.JsonNode
import dev.niessen.webhookservice.exception.exceptions.InvalidJsonFieldException
import dev.niessen.webhookservice.model.MenuModel
import dev.niessen.webhookservice.model.MenuProperty
import dev.niessen.webhookservice.model.MenuRestaurant
import dev.niessen.webhookservice.utils.TimeUtils
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class PaceJsonToModelConverter {

    private val timeUtils = TimeUtils()

    fun convert(json: JsonNode): List<MenuModel> {
        val currentDateString = getCurrentDateString(json)
        val menuItems = getTodaysMenuItems(json, currentDateString)

        return menuItems.map { convertToMenuItem(it) }
    }

    private fun convertToMenuItem(jsonNode: JsonNode): MenuModel {
        val menuNameAndDescription = jsonNode.get("GastDesc_de")?.asText(null) ?: throw InvalidJsonFieldException("GastDesc_de")

        val (menuName, menuDescription) = parseMenuNameAndDescription(menuNameAndDescription)
        val restaurant = MenuRestaurant.byName(jsonNode.get("outlet")?.asText(null))

        val subtitle = jsonNode.get("MenuName")?.asText(null)
        val price = jsonNode.get("ProductPrice")?.asText(null)?.run { this + "€" }
        val properties = parseProperties(jsonNode)

        return MenuModel(
            menuName = menuName,
            restaurant = restaurant,
            description = menuDescription,
            subtitle = subtitle,
            price = price,
            properties = properties
        )
    }

    private fun parseProperties(dataEntry: JsonNode): List<MenuProperty> {
        val jsonProperties = dataEntry.get("merkmale") ?: return emptyList()
        val keys = jsonProperties.properties().map { it.key }
        return keys.mapNotNull { MenuProperty.byName(it) }
    }

    private fun parseMenuNameAndDescription(menuNameAndDescription: String): Pair<String, String?> {
        val split = menuNameAndDescription.replace("\n", "").split("|", limit = 2)
        return split[0].trim() to split.getOrNull(1)?.trimStart()
    }

    private fun getTodaysMenuItems(json: JsonNode, currentDateString: String): List<JsonNode> {
        val dataEntry = json.get("data") ?: throw InvalidJsonFieldException("data")
        if (!dataEntry.isArray) throw InvalidJsonFieldException("data")

        val validEntries = dataEntry.toList().filter {
            val dateNode = it.get("date") ?: throw InvalidJsonFieldException("data.date")
            val dateText = dateNode.asText(null) ?: throw InvalidJsonFieldException("date")
            return@filter dateText == currentDateString
        }

        return validEntries
    }

    private fun getCurrentDateString(json: JsonNode): String {
        val daySelector = json.get("daySelector") ?: throw InvalidJsonFieldException("daySelector")
        if (!daySelector.isArray) throw InvalidJsonFieldException("daySelector")

        val currentDateDay = timeUtils.currentDay()

        val currentDateString = daySelector.toList().map {
            it.get("date")?.asText(null) ?: throw InvalidJsonFieldException("date")
        }.find {
            val jsonDay = LocalDate.parse(it).dayOfYear
            return@find jsonDay == currentDateDay
        } ?: throw InvalidJsonFieldException("date (current date)")

        return currentDateString
    }

}
