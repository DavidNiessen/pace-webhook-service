package dev.niessen.webhookservice.exception

import dev.niessen.webhookservice.exception.exceptions.JsonParseException
import dev.niessen.webhookservice.exception.exceptions.ServiceException
import dev.niessen.webhookservice.service.PaceService
import dev.niessen.webhookservice.utils.TestUtils
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.client.postForEntity
import org.springframework.web.servlet.resource.NoResourceFoundException

const val DISPATCH_ENDPOINT = "/api/v1/dispatch"

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ExceptionHandlerIT(
    @LocalServerPort val port: Int,
) {

    @MockitoBean
    private lateinit var paceService: PaceService

    @Test
    fun `handles ServiceException`() {
        val response = testForException(ServiceException("abc123", HttpStatus.I_AM_A_TEAPOT))

        assertThat(response.statusCode, `is`(HttpStatus.I_AM_A_TEAPOT))
        assertThat(response.body?.detail, `is`("abc123"))
    }

    @Test
    fun `handles JsonParseException redacts details if json is not provided by user with status 500`() {
        val response = testForException(JsonParseException("mySecretPassword", false))

        assertThat(response.statusCode, `is`(HttpStatus.INTERNAL_SERVER_ERROR))
        assertThat(response.body?.detail, not(containsString("mySecretPassword")))
    }

    @Test
    fun `handles JsonParseException does not redact details if json is provided by user with status 400`() {
        val response = testForException(JsonParseException("mySecretPassword", true))

        assertThat(response.statusCode, `is`(HttpStatus.BAD_REQUEST))
        assertThat(response.body?.detail, containsString("mySecretPassword"))
    }

    @Test
    fun `handles NoResourceFoundException`() {
        val response = testForException(NoResourceFoundException(HttpMethod.GET, "path"))

        assertThat(response.statusCode, `is`(HttpStatus.NOT_FOUND))
        assertThat(response.body?.detail, `is`("Resource not found"))
    }

    @Test
    fun `handles HttpRequestMethodNotSupportedException`() {
        val response = testForException(HttpRequestMethodNotSupportedException("GET"))

        assertThat(response.statusCode, `is`(HttpStatus.METHOD_NOT_ALLOWED))
        assertThat(response.body?.detail, `is`("Request method 'GET' is not supported"))
    }

    @Test
    fun `handles generic exception`() {
        val response = testForException(RuntimeException())

        assertThat(response.statusCode, `is`(HttpStatus.INTERNAL_SERVER_ERROR))
        assertThat(response.body?.detail, `is`("An unexpected error occurred"))
    }

    private fun testForException(exception: Exception): ResponseEntity<ProblemDetail> {
        `when`(paceService.getAndDispatchTodaysMenu()).thenAnswer {
            throw exception
        }

        return TestUtils.httpClient.postForEntity<ProblemDetail>("http://localhost:$port${DISPATCH_ENDPOINT}")
    }
}
