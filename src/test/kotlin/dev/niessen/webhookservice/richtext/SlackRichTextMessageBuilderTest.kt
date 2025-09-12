package dev.niessen.webhookservice.richtext

import dev.niessen.webhookservice.model.MenuModel
import dev.niessen.webhookservice.model.MenuProperty.*
import dev.niessen.webhookservice.model.MenuRestaurant.CANTEEN
import dev.niessen.webhookservice.model.MenuRestaurant.PAPA
import dev.niessen.webhookservice.properties.IconProperties
import dev.niessen.webhookservice.utils.IconUtil
import dev.niessen.webhookservice.utils.TimeUtils
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class SlackRichTextMessageBuilderTest {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val slackRichTextMessageFactory = SlackRichTextMessageBuilder("v1.0.0", IconUtil(IconProperties(
        mapOf(
            VEGETARIAN.propertyKey to "localhost/vegetarian",
            VEGAN.propertyKey to "localhost/vegan",
            PORK.propertyKey to "localhost/pork",
            // don't define url for no_sugar
        )
    )), TimeUtils())

    @Test
    fun `build slack menu message`() {
        val menu = listOf(
            MenuModel(
                menuName = "Rinderschmorbraten",
                restaurant = PAPA,
                description = "frisches Gemüse | Kartoffelgratin | Balsamico-Jus",
                subtitle = "FF PAPA KOCHT_",
                price = 12.00,
                properties = listOf()
            ),
            MenuModel(
                menuName = "Kichererbsen-Chili",
                restaurant = PAPA,
                description = "Quinoa | hausgemachte Röstzwiebeln",
                subtitle = "FF VEGETARISCH_",
                price = 9.00,
                properties = listOf(VEGETARIAN, VEGAN)
            ),
            MenuModel(
                menuName = "Schweinefilet",
                restaurant = CANTEEN,
                description = "Glasierte Äpfel | Kartoffelmousseline",
                subtitle = "COUNTER 2.1 Meat",
                price = 12.00,
                properties = listOf(PORK)
            )
        )

        val message = slackRichTextMessageFactory.buildMessage(menu)
        logger.info("\n$message")

        assertThat(message, `is`("""
            {"blocks":[{"type":"header","text":{"type":"plain_text","text":"Heutige Speisekarte (12.09.2025)","emoji":true}},{"type":"header","text":{"type":"plain_text","text":"Papa","emoji":true}},{"type":"divider"},{"type":"section","text":{"type":"mrkdwn","text":"• *Rinderschmorbraten*: frisches Gemüse | Kartoffelgratin | Balsamico-Jus"}},{"type":"context","elements":[{"type":"plain_text","text":"12.00€ ","emoji":true}]},{"type":"section","text":{"type":"mrkdwn","text":"• *Kichererbsen-Chili*: Quinoa | hausgemachte Röstzwiebeln"}},{"type":"context","elements":[{"type":"plain_text","text":"9.00€  ","emoji":true},{"type":"image","image_url":"localhost/vegan","alt_text":"Vegan"},{"type":"image","image_url":"localhost/vegetarian","alt_text":"Vegetarisch"}]},{"type":"header","text":{"type":"plain_text","text":"Kantine","emoji":true}},{"type":"divider"},{"type":"section","text":{"type":"mrkdwn","text":"• *Schweinefilet*: Glasierte Äpfel | Kartoffelmousseline"}},{"type":"context","elements":[{"type":"plain_text","text":"12.00€ ","emoji":true},{"type":"image","image_url":"localhost/pork","alt_text":"Schweinefleisch"}]},{"type":"divider"},{"type":"context","elements":[{"type":"image","image_url":"localhost/vegan","alt_text":"Vegan"},{"type":"plain_text","text":"Vegan","emoji":true},{"type":"image","image_url":"localhost/vegetarian","alt_text":"Vegetarisch"},{"type":"plain_text","text":"Vegetarisch","emoji":true},{"type":"image","image_url":"localhost/pork","alt_text":"Schweinefleisch"},{"type":"plain_text","text":"Schweinefleisch","emoji":true}]},{"type":"context","elements":[{"type":"mrkdwn","text":"Version v1.0.0 - 12.09.2025"},{"type":"mrkdwn","text":"<https://github.com/DavidNiessen/pace-webhook-service|source code>"}]}]}
        """.trimIndent()))
    }

    @Test
    fun `build slack menu message with missing non-critical properties`() {
        val menu = listOf(
            MenuModel(
                menuName = "Rinderschmorbraten",
                restaurant = CANTEEN,
                description = null,
                subtitle = null,
                price = null,
                properties = listOf()
            ),
            MenuModel(
                menuName = "Gemüseteller",
                restaurant = PAPA,
                description = null,
                subtitle = null,
                price = null,
                properties = listOf(VEGAN, PORK)
            ),
        )

        val message = slackRichTextMessageFactory.buildMessage(menu)
        logger.info("\n$message")

        assertThat(message, `is`("""
            {"blocks":[{"type":"header","text":{"type":"plain_text","text":"Heutige Speisekarte (12.09.2025)","emoji":true}},{"type":"header","text":{"type":"plain_text","text":"Papa","emoji":true}},{"type":"divider"},{"type":"section","text":{"type":"mrkdwn","text":"• *Gemüseteller*"}},{"type":"context","elements":[{"type":"image","image_url":"localhost/vegan","alt_text":"Vegan"},{"type":"image","image_url":"localhost/pork","alt_text":"Schweinefleisch"}]},{"type":"header","text":{"type":"plain_text","text":"Kantine","emoji":true}},{"type":"divider"},{"type":"section","text":{"type":"mrkdwn","text":"• *Rinderschmorbraten*"}},{"type":"divider"},{"type":"context","elements":[{"type":"image","image_url":"localhost/vegan","alt_text":"Vegan"},{"type":"plain_text","text":"Vegan","emoji":true},{"type":"image","image_url":"localhost/vegetarian","alt_text":"Vegetarisch"},{"type":"plain_text","text":"Vegetarisch","emoji":true},{"type":"image","image_url":"localhost/pork","alt_text":"Schweinefleisch"},{"type":"plain_text","text":"Schweinefleisch","emoji":true}]},{"type":"context","elements":[{"type":"mrkdwn","text":"Version v1.0.0 - 12.09.2025"},{"type":"mrkdwn","text":"<https://github.com/DavidNiessen/pace-webhook-service|source code>"}]}]}
        """.trimIndent()))
    }

    @Test
    fun `handle unknown icon`() {
        val menu = listOf(
            MenuModel(
                menuName = "Gemüseteller",
                restaurant = PAPA,
                description = "description",
                subtitle = "subtitle",
                price = 150.0,
                properties = listOf(NO_SUGAR)
            ),
        )

        val message = slackRichTextMessageFactory.buildMessage(menu)
        logger.info("\n$message")

        assertThat(message, `is`("""
            {"blocks":[{"type":"header","text":{"type":"plain_text","text":"Heutige Speisekarte (12.09.2025)","emoji":true}},{"type":"header","text":{"type":"plain_text","text":"Papa","emoji":true}},{"type":"divider"},{"type":"section","text":{"type":"mrkdwn","text":"• *Gemüseteller*: description"}},{"type":"context","elements":[{"type":"plain_text","text":"150.00€","emoji":true}]},{"type":"divider"},{"type":"context","elements":[{"type":"image","image_url":"localhost/vegan","alt_text":"Vegan"},{"type":"plain_text","text":"Vegan","emoji":true},{"type":"image","image_url":"localhost/vegetarian","alt_text":"Vegetarisch"},{"type":"plain_text","text":"Vegetarisch","emoji":true},{"type":"image","image_url":"localhost/pork","alt_text":"Schweinefleisch"},{"type":"plain_text","text":"Schweinefleisch","emoji":true}]},{"type":"context","elements":[{"type":"mrkdwn","text":"Version v1.0.0 - 12.09.2025"},{"type":"mrkdwn","text":"<https://github.com/DavidNiessen/pace-webhook-service|source code>"}]}]}
        """.trimIndent()))
    }

}
