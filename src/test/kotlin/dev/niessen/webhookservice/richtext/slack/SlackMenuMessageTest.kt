package dev.niessen.webhookservice.richtext.slack

import dev.niessen.webhookservice.model.MenuModel
import dev.niessen.webhookservice.model.MenuProperty.*
import dev.niessen.webhookservice.model.MenuRestaurant.CANTEEN
import dev.niessen.webhookservice.model.MenuRestaurant.PAPA
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class SlackMenuMessageTest {

    private val slackMenuMessage = SlackMenuMessage()

    @Test
    fun `build slack menu message`() {
        val menu = listOf(
            MenuModel(
                menuName = "Rinderschmorbraten",
                restaurant = PAPA,
                description = "frisches Gemüse | Kartoffelgratin | Balsamico-Jus",
                subtitle = "FF PAPA KOCHT_",
                price = "12.00€",
                properties = listOf()
            ),
            MenuModel(
                menuName = "Kichererbsen-Chili",
                restaurant = PAPA,
                description = "Quinoa | hausgemachte Röstzwiebeln",
                subtitle = "FF VEGETARISCH_",
                price = "9.00€",
                properties = listOf(VEGETARIAN, VEGAN, NO_SUGAR)
            ),
            MenuModel(
                menuName = "Schweinefilet",
                restaurant = CANTEEN,
                description = "Glasierte Äpfel | Kartoffelmousseline",
                subtitle = "COUNTER 2.1 Meat",
                price = "12.00€",
                properties = listOf(PORK)
            )
        )

        val message = slackMenuMessage.buildSlackMenuMessage(menu)

        assertThat(message, `is`("""
            {"blocks":[{"type":"header","text":{"type":"plain_text","text":"Heutige Speisekarte (11.09.2025)","emoji":true}},{"type":"header","text":{"type":"plain_text","text":"Papa","emoji":true}},{"type":"divider"},{"type":"section","text":{"type":"mrkdwn","text":"*Rinderschmorbraten*: frisches Gemüse | Kartoffelgratin | Balsamico-Jus"}},{"type":"section","text":{"type":"mrkdwn","text":"*Kichererbsen-Chili*: Quinoa | hausgemachte Röstzwiebeln"}},{"type":"context","elements":[{"type":"plain_text","text":"9.00€","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Vegetarisch"},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegan.png?raw=true","alt_text":"Vegan"},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/sugar_free.png?raw=true","alt_text":"Ohne Zucker"}]},{"type":"header","text":{"type":"plain_text","text":"Kantine","emoji":true}},{"type":"divider"},{"type":"section","text":{"type":"mrkdwn","text":"*Schweinefilet*: Glasierte Äpfel | Kartoffelmousseline"}},{"type":"context","elements":[{"type":"plain_text","text":"12.00€","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/pork.png?raw=true","alt_text":"Schweinefleisch"}]},{"type":"divider"},{"type":"context","elements":[{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegan.png?raw=true","alt_text":"Vegan"},{"type":"plain_text","text":"Vegan","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Vegetarisch"},{"type":"plain_text","text":"Vegetarisch","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/pork.png?raw=true","alt_text":"Schweinefleisch"},{"type":"plain_text","text":"Schweinefleisch","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/sugar_free.png?raw=true","alt_text":"Ohne Zucker"},{"type":"plain_text","text":"Ohne Zucker","emoji":true}]}]}
        """.trimIndent()))
    }

}
