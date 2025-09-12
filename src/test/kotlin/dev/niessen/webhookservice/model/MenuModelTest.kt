package dev.niessen.webhookservice.model

import dev.niessen.webhookservice.model.MenuProperty.*
import dev.niessen.webhookservice.model.MenuRestaurant.*
import dev.niessen.webhookservice.utils.Counter
import dev.niessen.webhookservice.utils.assertWithCounter
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class MenuModelTest {

    @Test
    fun `should return correct restaurant`() {
        val counter = Counter(MenuRestaurant.entries.size)
        assertWithCounter(MenuRestaurant.byName("papa"), `is`(PAPA), counter)
        assertWithCounter(MenuRestaurant.byName("canteen"), `is`(CANTEEN), counter)
        assertWithCounter(MenuRestaurant.byName("journaliStenClub"), `is`(JOURNALIST_CLUB), counter)
        assertWithCounter(MenuRestaurant.byName("diner"), `is`(DINER), counter)
        assertWithCounter(MenuRestaurant.byName("deli"), `is`(DELI), counter)
        assertWithCounter(MenuRestaurant.byName("cafe"), `is`(CAFE), counter)
        assertWithCounter(MenuRestaurant.byName("1234"), `is`(UNKNOWN), counter)
        counter.assert()
    }

    @Test
    fun `should return correct property`() {
        val counter = Counter(MenuProperty.entries.size)
        assertWithCounter(MenuProperty.byName("vegan"), `is`(VEGAN), counter)
        assertWithCounter(MenuProperty.byName("vegEtarIsch"), `is`(VEGETARIAN), counter)
        assertWithCounter(MenuProperty.byName("schwein"), `is`(PORK), counter)
        assertWithCounter(MenuProperty.byName("ohne_zucker"), `is`(NO_SUGAR), counter)
        assertThat(MenuProperty.byName("123"), nullValue())
        counter.assert()
    }
}
