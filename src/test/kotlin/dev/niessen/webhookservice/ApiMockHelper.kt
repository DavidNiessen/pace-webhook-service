package dev.niessen.webhookservice

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import dev.niessen.webhookservice.repository.PaceRepository
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import wiremock.org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

const val PACE_API_PATH_REGEX = "/api/foodfinder/list?.*"
const val SLACK_WEBHOOK_PATH_REGEX = "/services/.*/.*/.*"

object ApiMockHelper {

    private val objectMapper = ObjectMapper()


    fun stubForPaceApi(wireMockServer: WireMockServer) {
        val body = IOUtils.resourceToString(
            "responses/pace_response.json",
            StandardCharsets.UTF_8,
            ApiMockHelper.javaClass.classLoader
        )

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlMatching(PACE_API_PATH_REGEX))
                .withHeader("Apikey", equalTo("LPFZLrjRbo5Kry"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withBody(body)
                )
        )

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlMatching(PACE_API_PATH_REGEX))
                .atPriority(10)
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(401)
                )
        )
    }

    fun stubForSlackWebHook(wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            WireMock.post(WireMock.urlMatching(SLACK_WEBHOOK_PATH_REGEX))
                .atPriority(10)
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                )
        )

        wireMockServer.stubFor(
            WireMock.post(WireMock.urlMatching(SLACK_WEBHOOK_PATH_REGEX))
                .withHeader("Test-Fail", equalTo("true"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(401)
                )
        )
    }

    fun mockPaceApi(): PaceRepository {
        val body = IOUtils.resourceToString(
            "responses/pace_response.json",
            StandardCharsets.UTF_8,
            ApiMockHelper.javaClass.classLoader
        )
        val paceRepository = mock(PaceRepository::class.java)

        `when`(paceRepository.fetchPaceJson()).thenAnswer {
            objectMapper.readTree(body)
        }

        return paceRepository
    }
}
