package dev.niessen.webhookservice.cloudfunction

import dev.niessen.webhookservice.DeploymentType
import dev.niessen.webhookservice.converter.PaceJsonToModelConverter
import dev.niessen.webhookservice.repository.PaceRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CloudFunctionDispatcher(
    private val paceRepository: PaceRepository,
    private val paceJsonToModelConverter: PaceJsonToModelConverter,
    @Value("\${application.deployment.type}") private val deploymentType: DeploymentType,
) {

    @PostConstruct
    fun init() {
        if (deploymentType == DeploymentType.CLOUD_FUNCTION) {
            runApp()
        }
    }

    private fun runApp() {
        val models = paceJsonToModelConverter.convert(paceRepository.fetchPaceJson())
        println(models)
    }

}
