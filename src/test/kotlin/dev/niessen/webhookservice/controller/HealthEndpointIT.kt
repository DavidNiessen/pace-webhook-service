package dev.niessen.webhookservice.controller

import dev.niessen.webhookservice.utils.TestUtils
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.getForEntity

const val HEALTH_ENDPOINT = "/actuator/health"

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class HealthCheckIT(
    @LocalServerPort private val port: Int,
) {

    @Test
    fun `test health endpoint`() {
        val responseEntity = TestUtils.httpClient
            .getForEntity<String>("http://localhost:$port$HEALTH_ENDPOINT")

        assertThat(responseEntity.statusCode, `is`(HttpStatus.OK))
        assertThat(responseEntity.body, `is`("""{"status":"UP"}"""))
    }

}
