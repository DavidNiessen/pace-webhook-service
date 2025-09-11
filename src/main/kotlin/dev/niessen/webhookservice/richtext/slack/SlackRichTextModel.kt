package dev.niessen.webhookservice.richtext.slack

import com.fasterxml.jackson.databind.ObjectMapper
import dev.niessen.webhookservice.exception.exceptions.JsonParseException

private val objectMapper = ObjectMapper()

sealed interface Block
data class HeaderBlock(val text: PlainText) : Block
data object DividerBlock : Block
data class ContextBlock(val elements: List<Element>) : Block
data class SectionBlock(val text: MrkdwnText) : Block

sealed interface Element
data class PlainText(val text: String, val emoji: Boolean = true)
data class MrkdwnText(val text: String) : Element
data class ImageElement(val imageUrl: String, val altText: String) : Element
data class PlainTextElement(val text: String, val emoji: Boolean = true) : Element

class SlackMessageBuilder {
    val blocks = mutableListOf<Block>()

    fun header(text: String, emoji: Boolean = true) {
        blocks += HeaderBlock(PlainText(text, emoji))
    }

    fun divider() {
        blocks += DividerBlock
    }

    fun context(vararg elements: Element) {
        blocks += ContextBlock(elements.toList())
    }

    fun sectionMrkdwn(text: String) {
        blocks += SectionBlock(MrkdwnText(text))
    }

    fun build(): Map<String, Any> {
        return mapOf("blocks" to blocks.map { it.toJson() })
    }
}

fun Block.toJson(): Map<String, Any> = when (this) {
    is HeaderBlock -> mapOf(
        "type" to "header",
        "text" to mapOf(
            "type" to "plain_text",
            "text" to text.text,
            "emoji" to text.emoji
        )
    )
    is DividerBlock -> mapOf("type" to "divider")
    is ContextBlock -> mapOf(
        "type" to "context",
        "elements" to elements.map { it.toJson() }
    )
    is SectionBlock -> mapOf(
        "type" to "section",
        "text" to mapOf(
            "type" to "mrkdwn",
            "text" to text.text
        )
    )
}

fun Element.toJson(): Map<String, Any> = when (this) {
    is MrkdwnText -> mapOf("type" to "mrkdwn", "text" to text)
    is ImageElement -> mapOf("type" to "image", "image_url" to imageUrl, "alt_text" to altText)
    is PlainTextElement -> mapOf("type" to "plain_text", "text" to text, "emoji" to emoji)
}

fun slackMessage(build: SlackMessageBuilder.() -> Unit): String {
    val jsonString = objectMapper.writeValueAsString(SlackMessageBuilder().apply(build).build())
    if (jsonString.isNullOrBlank()) {
        throw JsonParseException(jsonString, false)
    }

    return jsonString
}
