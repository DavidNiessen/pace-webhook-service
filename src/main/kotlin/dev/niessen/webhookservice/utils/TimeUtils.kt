package dev.niessen.webhookservice.utils

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TimeUtils {

    fun currentDay() = LocalDateTime.now(ZoneOffset.UTC).dayOfYear
    fun formattedDate(): String = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

}
