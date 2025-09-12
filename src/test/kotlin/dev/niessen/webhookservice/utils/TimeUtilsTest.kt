package dev.niessen.webhookservice.utils

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TimeUtilsTest {

    private val timeUtils = TimeUtils()

    @Test
    fun `gets current day of year correctly`() {
        val today = LocalDateTime.now(ZoneOffset.UTC).dayOfYear
        assertThat(timeUtils.currentDay(), `is`(today))
    }

    @Test
    fun `formats date correctly`() {
        val today = LocalDateTime.now(ZoneOffset.UTC)

        val dayOfMonthFormatter = DateTimeFormatter.ofPattern("dd")
        val monthFormatter = DateTimeFormatter.ofPattern("MM")

        val dayOfMonth = dayOfMonthFormatter.format(today)
        val month = monthFormatter.format(today)

        val formattedDate = timeUtils.formattedDate()

        assertThat(formattedDate, `is`("$dayOfMonth.$month.${today.year}"))
    }

}
