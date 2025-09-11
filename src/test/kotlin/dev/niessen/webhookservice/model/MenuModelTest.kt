package dev.niessen.webhookservice.model

import dev.niessen.webhookservice.model.MenuProperty.*
import dev.niessen.webhookservice.model.MenuRestaurant.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class MenuModelTest {

    @Test
    fun `should return correct restaurant`() {
        assertThat(MenuRestaurant.byName("papa"), `is`(PAPA))
        assertThat(MenuRestaurant.byName("canteen"), `is`(CANTEEN))
        assertThat(MenuRestaurant.byName("journaliStenClub"), `is`(JOURNALIST_CLUB))
        assertThat(MenuRestaurant.byName("diner"), `is`(DINER))
        assertThat(MenuRestaurant.byName("deli"), `is`(DELI))
        assertThat(MenuRestaurant.byName("cafe"), `is`(CAFE))
        assertThat(MenuRestaurant.byName("1234"), `is`(UNKNOWN))
    }

    @Test
    fun `should return correct property`() {
        assertThat(MenuProperty.byName("vegan"), `is`(VEGAN))
        assertThat(MenuProperty.byName("vegEtarIsch"), `is`(VEGETARIAN))
        assertThat(MenuProperty.byName("schwein"), `is`(PORK))
        assertThat(MenuProperty.byName("ohne_zucker"), `is`(NO_SUGAR))
        assertThat(MenuProperty.byName("123"), nullValue())
    }
}
