package dev.niessen.webhookservice.utils

import java.net.URI
import java.time.Instant

object URLHelper {

    fun constructPaceUrl(paceHost: String, pacePath: String, pacePort: Int, https: Boolean = true): URI {
        val currentMillis = Instant.now().toEpochMilli()
        val protocol = if (https) "https" else "http"
        val replacedPath = pacePath.replace("{MILLIS}", currentMillis.toString())
        val url = "${protocol}://${paceHost}:${pacePort}/${replacedPath}"
        return URI.create(url)
    }

}
