package dev.niessen.webhookservice.utils

import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Component
class TimeUtils {

    fun today(): LocalDate = LocalDate.now(ZoneOffset.UTC)

    fun formatDate(date: LocalDate): String =
        date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

}
