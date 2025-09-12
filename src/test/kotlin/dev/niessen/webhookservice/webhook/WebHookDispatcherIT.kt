package dev.niessen.webhookservice.webhook

import com.github.tomakehurst.wiremock.client.WireMock.*
import dev.niessen.webhookservice.IntegrationTestBase
import dev.niessen.webhookservice.exception.exceptions.RequestException
import dev.niessen.webhookservice.properties.WebHookEndpointProperties
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.util.MultiValueMap
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WebHookDispatcherIT : IntegrationTestBase() {

    @Autowired
    private lateinit var properties: WebHookEndpointProperties

    @Autowired
    private lateinit var webHookDispatcher: WebHookDispatcher

    @Test
    fun `should dispatch webhook with correct url and body`() {
        val requestBody = """{ "message": "abc" }"""
        val webHookUrl = properties.urlWebHookMap.keys.first()
        webHookDispatcher.dispatchWebHook(webHookUrl, requestBody)

        val urlPath = URI(webHookUrl).path

        verify(
            postRequestedFor(urlMatching(".*$urlPath.*"))
                .withRequestBody(equalTo(requestBody))
        )
    }

    @Test
    fun `should throw exception if response status code is not 2xx`() {
        val requestBody = """{ "message": "abc" }"""
        val webHookUrl = properties.urlWebHookMap.keys.first()

        val exception = assertThrows<RequestException> {
            webHookDispatcher.dispatchWebHook(
                webHookUrl, requestBody,
                MultiValueMap.fromSingleValue(mapOf("Test-Fail" to "true"))
            )
        }

        assertThat(exception.statusCode, `is`(HttpStatus.INTERNAL_SERVER_ERROR))
        assertThat(exception.message, containsString("401"))
    }

}
