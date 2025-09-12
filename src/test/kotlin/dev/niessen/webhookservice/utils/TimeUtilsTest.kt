package dev.niessen.webhookservice.utils

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class TimeUtilsTest {

    private val timeUtils = TimeUtils()

    @Test
    fun `gets current day of year correctly`() {
        val today = LocalDate.now()
        assertThat(timeUtils.today().toString(), `is`(today.toString()))
    }

    @Test
    fun `formats date correctly`() {
        val date = LocalDate.of(2025, 9, 12)

        val formattedDate = timeUtils.formatDate(date)

        assertThat(formattedDate, `is`("12.09.2025"))
    }

}
