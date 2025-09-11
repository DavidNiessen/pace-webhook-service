package dev.niessen.webhookservice.utils

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.startsWith
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.Instant

class URLHelperTest {

    private val pacePath = "api/foodfinder/list?_={MILLIS}"

    @ParameterizedTest
    @ValueSource(strings = [
        "api.dailypace.de",
        "example.com"
    ])
    fun `constructs pace url correctly with current millis`(url: String) {
        val constructedUrl = URLHelper.constructPaceUrl(url, pacePath, 443)
        val currentMillis = Instant.now().toEpochMilli()
        val currentMillisSubString = currentMillis.toString().substring(0, 5)

        assertThat(constructedUrl.toString(), startsWith("https://$url:443/api/foodfinder/list?_=$currentMillisSubString"))
    }

    @Test
    fun `constructs pace url without https correctly`() {
        val constructedUrl = URLHelper.constructPaceUrl("api.dailypace.de", pacePath, 883, false)

        assertThat(constructedUrl.toString(), startsWith("http://api.dailypace.de"))
    }

}
