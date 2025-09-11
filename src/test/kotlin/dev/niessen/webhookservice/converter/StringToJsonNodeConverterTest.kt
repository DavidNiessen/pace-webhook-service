package dev.niessen.webhookservice.converter

import com.fasterxml.jackson.databind.ObjectMapper
import dev.niessen.webhookservice.exception.exceptions.JsonParseException
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus

class StringToJsonNodeConverterTest {

    private val converter = StringToJsonNodeConverter(ObjectMapper())

    @Test
    fun `converts nested json string correctly`() {
        val jsonString = """
            {
                "car": {
                    "id": "12345"
                }
            }
        """.trimIndent()

        val jsonNode = converter.convert(jsonString)
        assertThat(jsonNode, notNullValue())

        val carNode = jsonNode.get("car")
        assertThat(carNode, notNullValue())

        val idNode = carNode.get("id")
        assertThat(idNode.asText(), `is`("12345"))
    }

    @Test
    fun `throws JsonParseException with code 400 if invalid json was provided by user`() {
        val invalidJsonString = "{ abc }"
        val exception = assertThrows<JsonParseException> {
            converter.convert(invalidJsonString, true)
        }

        assertThat(exception.statusCode,  `is`(HttpStatus.BAD_REQUEST))
    }

    @Test
    fun `throws JsonParseException with code 500 if invalid json was provided by service`() {
        val invalidJsonString = "{ abc }"
        val exception = assertThrows<JsonParseException> {
            converter.convert(invalidJsonString, false)
        }

        assertThat(exception.statusCode,  `is`(HttpStatus.INTERNAL_SERVER_ERROR))
    }

}
