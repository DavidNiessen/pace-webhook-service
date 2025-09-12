package dev.niessen.webhookservice.properties

import dev.niessen.webhookservice.webhook.WebHookType
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test

class WebHookEndPointPropertiesTest {

    @Test
    fun `builds correct url map same list size`() {
        val properties = WebHookEndpointProperties(
            listOf("url1", "url2"),
            listOf(WebHookType.SLACK, WebHookType.SLACK),
            443,
            0
        )

        val urlMap = properties.urlWebHookMap

        assertThat(urlMap["url1"], `is`(WebHookType.SLACK))
        assertThat(urlMap["url2"], `is`(WebHookType.SLACK))
    }

    @Test
    fun `builds correct url map different list size 1`() {
        val properties = WebHookEndpointProperties(
            listOf("url1", "url2"),
            listOf(WebHookType.SLACK),
            443,
            0
        )

        val urlMap = properties.urlWebHookMap

        assertThat(urlMap.keys, hasSize(1))
        assertThat(urlMap["url1"], `is`(WebHookType.SLACK))
        assertThat(urlMap["url2"], nullValue())
    }

    @Test
    fun `builds correct url map different list size 2`() {
        val properties = WebHookEndpointProperties(
            listOf("url1"),
            listOf(WebHookType.SLACK, WebHookType.SLACK),
            443,
            0
        )

        val urlMap = properties.urlWebHookMap

        assertThat(urlMap.keys, hasSize(1))
        assertThat(urlMap["url1"], `is`(WebHookType.SLACK))
        assertThat(urlMap["url2"], nullValue())
    }

    @Test
    fun `builds correct url empty list`() {
        val properties = WebHookEndpointProperties(
            listOf("url1", "url2"),
            listOf(),
            443,
            0
        )

        val urlMap = properties.urlWebHookMap

        assertThat(urlMap.keys, hasSize(0))
    }

    @Test
    fun `builds correct type group map`() {
        val properties = WebHookEndpointProperties(
            listOf("url1", "url2"),
            listOf(WebHookType.SLACK, WebHookType.SLACK),
            443,
            0
        )

        val urlMap = properties.typeGroupMap

        assertThat(urlMap.keys, hasSize(1))
        assertThat(urlMap[WebHookType.SLACK], hasSize(2))
    }

    @Test
    fun `getUrlsForType should return correct urls`() {
        val properties = WebHookEndpointProperties(
            listOf("url1", "url2"),
            listOf(WebHookType.SLACK, WebHookType.SLACK),
            443,
            0
        )

        val urls = properties.getUrlsForType(WebHookType.SLACK)
        assertThat(urls, hasSize(2))
    }

    @Test
    fun `typeList should return all types`() {
        val properties = WebHookEndpointProperties(
            listOf("url1", "url2"),
            listOf(WebHookType.SLACK, WebHookType.SLACK),
            443,
            0
        )

        val types = properties.typeList

        assertThat(types, hasSize(1))
        assertThat(types.first(), `is`(WebHookType.SLACK))
    }

}
