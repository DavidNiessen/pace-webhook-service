package dev.niessen.webhookservice.service

import com.github.tomakehurst.wiremock.client.WireMock.*
import dev.niessen.webhookservice.IntegrationTestBase
import dev.niessen.webhookservice.properties.PaceProperties
import dev.niessen.webhookservice.properties.WebHookEndpointProperties
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaceServiceIT : IntegrationTestBase() {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var paceService: PaceService

    @Autowired
    private lateinit var paceProperties: PaceProperties

    @Autowired
    private lateinit var webhookProperties: WebHookEndpointProperties

    @Test
    fun `e2e should fetch current menu and dispatch it to webhook`() {
        paceService.getAndDispatchTodaysMenu()

        // verify pace api call
        val paceUrlPath = paceProperties.path.substring(0, 8)
        verify(
            getRequestedFor(urlMatching(".*$paceUrlPath.*"))
                .withHeader("Apikey", equalTo(paceProperties.apiKey))
        )

        // verify webhook calls
        val webHookUrls = webhookProperties.urlWebHookMap.keys
        webHookUrls.forEach { webHookUrl ->
            val path = URI(webHookUrl).path
            val allRequests = findAll(postRequestedFor(urlMatching(".*$path.*")))

            // only one request should be made per url
            assertThat(allRequests, hasSize(1))

            val request = allRequests.first()
            // assert headers
            assertThat(request.header(HttpHeaders.CONTENT_TYPE).values,
                containsInAnyOrder("application/json"))

            // assert request body
            val body = request.bodyAsString
            logger.info("\n$body")
            assertThat(body, `is`("""
                {"blocks":[{"type":"header","text":{"type":"plain_text","text":"Heutige Speisekarte (12.09.2025)","emoji":true}},{"type":"header","text":{"type":"plain_text","text":"Papa","emoji":true}},{"type":"divider"},{"type":"section","text":{"type":"mrkdwn","text":"*Zuchtlachsfilet \"Finkenwerder Art\"* - Kartoffelpüree - frische Zitrone"}},{"type":"context","elements":[{"type":"plain_text","text":"12.00€ ","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/pork.png?raw=true","alt_text":"Schweinefleisch"},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/sugar_free.png?raw=true","alt_text":"Ohne Zucker"}]},{"type":"section","text":{"type":"mrkdwn","text":"*Putenbrust in Kräuterei* - Ofenkartoffeln - Feldsalat - Kräuteröl"}},{"type":"context","elements":[{"type":"plain_text","text":"12.00€ ","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/sugar_free.png?raw=true","alt_text":"Ohne Zucker"}]},{"type":"section","text":{"type":"mrkdwn","text":"*Rote-Bete-Falafel* - Bulgur - Kräuter-Joghurtdip"}},{"type":"context","elements":[{"type":"plain_text","text":"10.00€ ","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Vegetarisch"}]},{"type":"section","text":{"type":"mrkdwn","text":"*Vollkorn-Pasta* - Tomaten-Champignon-Sauce - veganer Gouda - Frisches Basilikum"}},{"type":"context","elements":[{"type":"plain_text","text":"9.00€  ","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegan.png?raw=true","alt_text":"Vegan"},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Vegetarisch"}]},{"type":"section","text":{"type":"mrkdwn","text":"*Gemüseteller*"}},{"type":"context","elements":[{"type":"plain_text","text":"7.50€  ","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegan.png?raw=true","alt_text":"Vegan"},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Vegetarisch"}]},{"type":"section","text":{"type":"mrkdwn","text":"*Maiscremesuppe*"}},{"type":"context","elements":[{"type":"plain_text","text":"4.30€  ","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegan.png?raw=true","alt_text":"Vegan"},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Vegetarisch"},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/sugar_free.png?raw=true","alt_text":"Ohne Zucker"}]},{"type":"section","text":{"type":"mrkdwn","text":"*Grießbrei* - Waldbeeren"}},{"type":"context","elements":[{"type":"plain_text","text":"4.30€  ","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Vegetarisch"}]},{"type":"section","text":{"type":"mrkdwn","text":"*Ofengemüse* - Champignons - Kürbis - Blumenkohl"}},{"type":"context","elements":[{"type":"plain_text","text":"2.00€  ","emoji":true}]},{"type":"section","text":{"type":"mrkdwn","text":"*Salatbuffet nach Gewicht* - 100 gr. / 1,80 €Salatbuffet veggie nach Gewicht - 100 gr. / 1,80 €"}},{"type":"context","elements":[{"type":"plain_text","text":"1.80€  ","emoji":true}]},{"type":"section","text":{"type":"mrkdwn","text":"*Birne Helene* - vegane Birne Helene"}},{"type":"context","elements":[{"type":"plain_text","text":"1.70€  ","emoji":true}]},{"type":"header","text":{"type":"plain_text","text":"Kantine","emoji":true}},{"type":"divider"},{"type":"section","text":{"type":"mrkdwn","text":"*Schweinefilet* - Glasierte Äpfel - Kartoffel-Mousseline"}},{"type":"context","elements":[{"type":"plain_text","text":"12.00€ ","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/pork.png?raw=true","alt_text":"Schweinefleisch"}]},{"type":"section","text":{"type":"mrkdwn","text":"*Warm geräucherter Lachs* - Kartoffelrösti - Schnittlauch-Crème fraîche"}},{"type":"context","elements":[{"type":"plain_text","text":"12.00€ ","emoji":true}]},{"type":"section","text":{"type":"mrkdwn","text":"*Vegane Kibbeh* - Tomaten-Kreuzkümmel-Salat - Cacik"}},{"type":"context","elements":[{"type":"plain_text","text":"11.00€ ","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegan.png?raw=true","alt_text":"Vegan"},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Vegetarisch"}]},{"type":"section","text":{"type":"mrkdwn","text":"*Käsespätzle* - Butterzwiebeln - Bunten Salat"}},{"type":"context","elements":[{"type":"plain_text","text":"9.50€  ","emoji":true}]},{"type":"divider"},{"type":"context","elements":[{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegan.png?raw=true","alt_text":"Vegan"},{"type":"plain_text","text":"Vegan","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/vegeterian.png?raw=true","alt_text":"Vegetarisch"},{"type":"plain_text","text":"Vegetarisch","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/pork.png?raw=true","alt_text":"Schweinefleisch"},{"type":"plain_text","text":"Schweinefleisch","emoji":true},{"type":"image","image_url":"https://github.com/DavidNiessen/pace-webhook-service/blob/main/assets/icons/sugar_free.png?raw=true","alt_text":"Ohne Zucker"},{"type":"plain_text","text":"Ohne Zucker","emoji":true}]},{"type":"context","elements":[{"type":"mrkdwn","text":"v1.0.0 - 12.09.2025"},{"type":"mrkdwn","text":"<https://github.com/DavidNiessen/pace-webhook-service|source code>"}]}]}
            """.trimIndent()))
        }
    }

}
