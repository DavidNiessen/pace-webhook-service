package dev.niessen.webhookservice.cloudfunction

import dev.niessen.webhookservice.DeploymentType
import dev.niessen.webhookservice.service.PaceService
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CloudFunctionDispatcher(
    private val paceService: PaceService,
    @Value("\${application.deployment.type}") private val deploymentType: DeploymentType,
) {

    @PostConstruct
    fun init() {
        if (deploymentType == DeploymentType.CLOUD_FUNCTION) {
            runApp()
        }
    }

    private fun runApp() {
        paceService.getAndDispatchTodaysMenu()
    }

}
