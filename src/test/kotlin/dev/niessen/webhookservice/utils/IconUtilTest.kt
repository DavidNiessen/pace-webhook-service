package dev.niessen.webhookservice.utils

import dev.niessen.webhookservice.model.MenuProperty
import dev.niessen.webhookservice.properties.IconProperties
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class IconUtilTest {

    private val iconUtil = IconUtil(
        IconProperties(
            mapOf(
                MenuProperty.VEGAN.propertyKey to "localhost/vegan",
                MenuProperty.PORK.propertyKey to "localhost/pork",
            )
        )
    )

    @Test
    fun `should get correct icon url`() {
        assertThat(iconUtil.propertyToIconUrl(MenuProperty.VEGAN), `is`("localhost/vegan"))
        assertThat(iconUtil.propertyToIconUrl(MenuProperty.PORK), `is`("localhost/pork"))
    }

    @Test
    fun `should return null if icon is not found`() {
        assertThat(iconUtil.propertyToIconUrl(MenuProperty.NO_SUGAR), nullValue())
    }

}
