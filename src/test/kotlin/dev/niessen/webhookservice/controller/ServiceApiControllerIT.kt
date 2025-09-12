package dev.niessen.webhookservice.controller

import dev.niessen.webhookservice.service.PaceService
import dev.niessen.webhookservice.utils.TestUtils
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.getForEntity
import org.springframework.web.client.postForEntity

const val DISPATCH_ENDPOINT = "/api/v1/dispatch"

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class ServiceApiControllerIT(
    @LocalServerPort val port: Int,
) {

    @MockitoBean
    private lateinit var paceService: PaceService

    @Test
    fun `dispatch endpoint should return 200 and call paceService`() {
        val responseEntity = TestUtils.httpClient
            .postForEntity<String>("http://localhost:$port$DISPATCH_ENDPOINT")

        assertThat(responseEntity.statusCode, `is`(HttpStatus.OK))
        verify(paceService, times(1)).getAndDispatchTodaysMenu()
    }

    @Test
    fun `dispatch endpoint should return 501 if called with wrong method`() {
        val responseEntity = TestUtils.httpClient
            .getForEntity<String>("http://localhost:$port$DISPATCH_ENDPOINT")

        assertThat(responseEntity.statusCode, `is`(HttpStatus.METHOD_NOT_ALLOWED))
        verify(paceService, times(0)).getAndDispatchTodaysMenu()
    }

}
