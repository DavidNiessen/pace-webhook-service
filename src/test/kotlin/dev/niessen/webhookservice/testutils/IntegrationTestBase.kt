package dev.niessen.webhookservice.testutils

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import dev.niessen.webhookservice.repository.PaceRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
abstract class IntegrationTestBase {

    @Autowired
    private lateinit var paceRepository: PaceRepository

    private lateinit var wireMockServer: WireMockServer


    @BeforeEach
    fun setupMockServer() {
        wireMockServer = WireMockServer(WireMockConfiguration.options().dynamicHttpsPort())
        wireMockServer.start()

        ApiMockHelper.stubForPaceApi(wireMockServer)
        paceRepository.properties.port = wireMockServer.port()
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }

}
