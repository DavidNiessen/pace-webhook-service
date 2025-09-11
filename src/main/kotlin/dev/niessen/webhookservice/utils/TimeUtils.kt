package dev.niessen.webhookservice.utils

import java.time.LocalDateTime
import java.time.ZoneOffset

class TimeUtils {

    fun currentDay() = LocalDateTime.now(ZoneOffset.UTC).dayOfYear

}
