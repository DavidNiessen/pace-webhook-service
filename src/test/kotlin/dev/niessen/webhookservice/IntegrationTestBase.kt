package dev.niessen.webhookservice

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import dev.niessen.webhookservice.repository.PaceRepository
import dev.niessen.webhookservice.utils.TimeUtils
import dev.niessen.webhookservice.webhook.WebHookDispatcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import java.time.LocalDate

@Component
abstract class IntegrationTestBase {

    val mockDate: LocalDate = LocalDate.of(2025, 9, 12)

    @Autowired
    private lateinit var paceRepository: PaceRepository

    @Autowired
    private lateinit var webHookDispatcher: WebHookDispatcher

    @MockitoSpyBean
    private lateinit var timeUtils: TimeUtils

    private lateinit var wireMockServer: WireMockServer


    @BeforeEach
    fun setupMockEnvironment() {
        wireMockServer = WireMockServer(WireMockConfiguration.options().dynamicHttpsPort())
        wireMockServer.start()
        WireMock.configureFor(wireMockServer.port())

        ApiMockHelper.stubForPaceApi(wireMockServer)
        ApiMockHelper.stubForSlackWebHook(wireMockServer)

        paceRepository.properties.port = wireMockServer.port()
        webHookDispatcher.properties.port = wireMockServer.port()

        mockCurrentDate()
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }

    private fun mockCurrentDate() {
        `when`(timeUtils.today()).thenReturn(mockDate)
    }
}
