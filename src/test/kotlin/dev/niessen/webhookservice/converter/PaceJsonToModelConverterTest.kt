package dev.niessen.webhookservice.converter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import dev.niessen.webhookservice.ApiMockHelper
import dev.niessen.webhookservice.exception.exceptions.InvalidJsonFieldException
import dev.niessen.webhookservice.model.MenuProperty
import dev.niessen.webhookservice.model.MenuRestaurant
import dev.niessen.webhookservice.properties.PaceProperties
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import wiremock.org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class PaceJsonToModelConverterTest {

    private val objectMapper = ObjectMapper()

    private lateinit var converter: PaceJsonToModelConverter

    @BeforeEach
    fun setUp() {
        converter = PaceJsonToModelConverter(
            PaceProperties(
                host = "host",
                path = "path",
                port = 443,
                apiKey = "apiKEy",
                timeoutMs = 0,
                userAgent = "userAgent",
                https = true,
                cacheTtlMs = 0,
                restaurantWhitelist = setOf(MenuRestaurant.PAPA, MenuRestaurant.CANTEEN),
                mealtimeWhitelist = setOf("Mittagessen"),
                menuLabelBlacklist = setOf("SAFT"),
            ),
            ApiMockHelper.mockTimeUtils(LocalDate.of(2025, 9, 11))
        )
    }

    @Test
    fun `convert to models success`() {
        val json = setupJson("responses/pace_response.json")
        val items = converter.convert(json)
        assertThat(items, hasSize(41))

        // assert single item
        val item = items.find { it.menuName == "Gegrillte Zucchini" }
        assertThat(item!!, notNullValue())
        assertThat(item.restaurant, `is`(MenuRestaurant.CANTEEN))
        assertThat(item.description, `is`("Halloumi - Kapern-Zitronen-Dressing"))
        assertThat(item.subtitle, `is`("COUNTER 1.1 Bowls_"))
        assertThat(item.price, `is`(11.0))
        // assert item properties
        val properties = item.properties
        assertThat(properties, hasSize(1))
        assertThat(properties[0], `is`(MenuProperty.VEGETARIAN))
    }

    @Test
    fun `continues if can't find not critical property`() {
        val json = setupJson("responses/pace_response_invalid_1.json")
        val items = converter.convert(json)
        assertThat(items, notNullValue())
        assertThat(items, hasSize(41))

        // property "MenuName" was removed, but converter should still continue
        val item = items.find { it.menuName == "Chocolate-Lava-Cookie" }
        assertThat(item!!, notNullValue())
        // should set invalid, non-critical field to null instead
        assertThat(item.subtitle, nullValue())
    }

    @Test
    fun `throws exception if can't find critical property`() {
        val json = setupJson("responses/pace_response_invalid_2.json")
        assertThrows<InvalidJsonFieldException> {
            converter.convert(json)
        }
    }

    @Test
    fun `should skip item if can't find critical property in single item`() {
        val json = setupJson("responses/pace_response_invalid_3.json")
        val items = converter.convert(json)

        assertThat(items, notNullValue())
        assertThat(items, hasSize(40))
    }

    private fun setupJson(fileName: String): JsonNode {
        val response = IOUtils.resourceToString(
            fileName,
            StandardCharsets.UTF_8,
            PaceJsonToModelConverterTest::class.java.classLoader
        )
        return objectMapper.readTree(response)
    }

}
