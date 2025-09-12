package dev.niessen.webhookservice.richtext

import dev.niessen.webhookservice.model.MenuModel
import dev.niessen.webhookservice.webhook.WebHookType

interface RichTextMessageBuilder {

    fun webHookType(): WebHookType

    fun buildMessage(menu: List<MenuModel>): String

}
