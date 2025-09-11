package dev.niessen.webhookservice.repository

import dev.niessen.webhookservice.exception.exceptions.PaceRequestException
import dev.niessen.webhookservice.testutils.IntegrationTestBase
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PaceRepositoryIT: IntegrationTestBase() {

    @Autowired
    private lateinit var repository: PaceRepository

    @Test
    fun `fetchPaceJson should return valid json`() {
        val json = repository.fetchPaceJson()
        assertThat(json, notNullValue())

        val data = json.get("data")
        assertThat(data, notNullValue())
    }

    @Test
    fun `fetchPaceJson should throw ServiceException if invalid api key`() {
        val exception = assertThrows<PaceRequestException> {
            repository.properties.apiKey = "invalidapikey"
            repository.init()
            repository.fetchPaceJson()
        }

        assertThat(exception.statusCode, `is`(HttpStatus.INTERNAL_SERVER_ERROR))
        assertThat(exception.message, containsString("401"))
    }

}
