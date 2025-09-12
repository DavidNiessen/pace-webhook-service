package dev.niessen.webhookservice.repository

import com.fasterxml.jackson.databind.JsonNode
import dev.niessen.webhookservice.IntegrationTestBase
import dev.niessen.webhookservice.converter.StringToJsonNodeConverter
import dev.niessen.webhookservice.exception.exceptions.RequestException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.web.client.RestTemplate
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaceRepositoryRetryIT : IntegrationTestBase() {

    @Mock
    private lateinit var httpClient: RestTemplate

    @Mock
    private lateinit var jsonNode: JsonNode

    @MockitoBean
    private lateinit var jsonConverter: StringToJsonNodeConverter

    @Autowired
    private lateinit var repository: PaceRepository

    @BeforeEach
    fun setup() {
        `when`(
            httpClient
                .exchange(any<URI>(), any<HttpMethod>(), any<HttpEntity<*>>(), any<Class<*>>())
        ).thenReturn(
            ResponseEntity("12345", HttpStatus.BAD_REQUEST)
        )
        `when`(jsonConverter.convert(any<String>(), any<Boolean>())).thenAnswer { jsonNode }

        ReflectionTestUtils.setField(repository, "httpClient", httpClient)
    }

    @Test
    fun `should retry 3 times on error`() {
        assertThrows<RequestException> {
            repository.fetchPaceJson()
        }

        verify(httpClient, times(3))
            .exchange(any<URI>(), any<HttpMethod>(), any<HttpEntity<*>>(), any<Class<*>>())
    }

}
