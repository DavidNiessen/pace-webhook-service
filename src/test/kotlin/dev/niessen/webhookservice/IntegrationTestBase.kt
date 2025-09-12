package dev.niessen.webhookservice

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import dev.niessen.webhookservice.repository.PaceRepository
import dev.niessen.webhookservice.webhook.WebHookDispatcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
abstract class IntegrationTestBase {

    @Autowired
    private lateinit var paceRepository: PaceRepository

    @Autowired
    private lateinit var webHookDispatcher: WebHookDispatcher

    private lateinit var wireMockServer: WireMockServer


    @BeforeEach
    fun setupMockServer() {
        wireMockServer = WireMockServer(WireMockConfiguration.options().dynamicHttpsPort())
        wireMockServer.start()
        WireMock.configureFor(wireMockServer.port())

        ApiMockHelper.stubForPaceApi(wireMockServer)
        ApiMockHelper.stubForSlackWebHook(wireMockServer)

        paceRepository.properties.port = wireMockServer.port()
        webHookDispatcher.properties.port = wireMockServer.port()
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }
}
