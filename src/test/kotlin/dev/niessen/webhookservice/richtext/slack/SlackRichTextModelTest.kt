package dev.niessen.webhookservice.richtext.slack

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class SlackRichTextModelTest {

    @Test
    fun `build slack rich text`() {
        val message = slackMessage {
            header("Heutige Speisekarte (11.9.2025)")
            header("Papa")
            divider()
            context(MrkdwnText("FF PAPA KOCHT_"))
            sectionMrkdwn("*Rinderschmorbraten*\n>frisches Gemüse | Kartoffelgratin | Balsamico-Jus\n *1.30€*")
            context(
                ImageElement("https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true", "Test"),
                ImageElement("https://raw.githubusercontent.com/DavidNiessen/pace-webhook-service/refs/heads/main/assets/icons/pork.png", "Test"),
                PlainTextElement("2 votes")
            )
            header("Kantine")
            divider()
            context(MrkdwnText("FF PAPA KOCHT_"))
            sectionMrkdwn("*Rinderschmorbraten*\n>frisches Gemüse | Kartoffelgratin | Balsamico-Jus\n *1.30€*")
            context(
                ImageElement("https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true", "Test"),
                PlainTextElement("Vegetarisch"),
                ImageElement("https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true", "Test"),
                PlainTextElement("Vegan"),
                ImageElement("https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true", "Test"),
                PlainTextElement("Zuckerfrei"),
                ImageElement("https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true", "Test"),
                PlainTextElement("Schwein")
            )
        }

        assertThat(message, `is`("""
            {"blocks":[{"type":"header","text":{"type":"plain_text","text":"Heutige Speisekarte (11.9.2025)","emoji":true}},{"type":"header","text":{"type":"plain_text","text":"Papa","emoji":true}},{"type":"divider"},{"type":"context","elements":[{"type":"mrkdwn","text":"FF PAPA KOCHT_"}]},{"type":"section","text":{"type":"mrkdwn","text":"*Rinderschmorbraten*\n>frisches Gemüse | Kartoffelgratin | Balsamico-Jus\n *1.30€*"}},{"type":"context","elements":[{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Test"},{"type":"image","image_url":"https://raw.githubusercontent.com/DavidNiessen/pace-webhook-service/refs/heads/main/assets/icons/pork.png","alt_text":"Test"},{"type":"plain_text","text":"2 votes","emoji":true}]},{"type":"header","text":{"type":"plain_text","text":"Kantine","emoji":true}},{"type":"divider"},{"type":"context","elements":[{"type":"mrkdwn","text":"FF PAPA KOCHT_"}]},{"type":"section","text":{"type":"mrkdwn","text":"*Rinderschmorbraten*\n>frisches Gemüse | Kartoffelgratin | Balsamico-Jus\n *1.30€*"}},{"type":"context","elements":[{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Test"},{"type":"plain_text","text":"Vegetarisch","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Test"},{"type":"plain_text","text":"Vegan","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Test"},{"type":"plain_text","text":"Zuckerfrei","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Test"},{"type":"plain_text","text":"Schwein","emoji":true}]}]}
        """.trimIndent()))
    }

}
