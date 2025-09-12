package dev.niessen.webhookservice.controller

import dev.niessen.webhookservice.service.PaceService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class ServiceApiController(
    private val paceService: PaceService,
) {

    @PostMapping("/dispatch")
    fun dispatchWebHooks() {
        paceService.getAndDispatchTodaysMenu()
    }

}
