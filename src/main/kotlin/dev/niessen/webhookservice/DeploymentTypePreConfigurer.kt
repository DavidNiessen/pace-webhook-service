package dev.niessen.webhookservice

import dev.niessen.webhookservice.DeploymentType.*
import dev.niessen.webhookservice.exception.exceptions.InvalidDeploymentTypeException
import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.StandardEnvironment
import org.springframework.core.io.support.ResourcePropertySource

const val DEPLOYMENT_TYPE_PROPERTY_KEY = "application.deployment.type"

object DeploymentTypePreConfigurer {

    fun configureAppByDeploymentType(application: SpringApplication, standardEnvironment: StandardEnvironment) =
        configureDefaultEnvironment(application, standardEnvironment) { environment ->
            environment.propertySources.addLast(ResourcePropertySource("classpath:application.properties"))

            val deploymentTypeString = environment.getProperty(DEPLOYMENT_TYPE_PROPERTY_KEY)
            val deploymentType = DeploymentType.findByName(deploymentTypeString)
                ?: throw InvalidDeploymentTypeException(deploymentTypeString)

            val webApplicationType: WebApplicationType
            val propertyMap = mutableMapOf<String, Any>()


            when (deploymentType) {
                CLOUD_FUNCTION -> {
                    webApplicationType = WebApplicationType.NONE
                }

                DAEMON -> {
                    webApplicationType = WebApplicationType.NONE
                }

                REST -> {
                    webApplicationType = WebApplicationType.SERVLET
                }
            }

            application.webApplicationType = webApplicationType
            environment.propertySources.addLast(MapPropertySource("deploymentTypeProperties", propertyMap))
        }

    private fun configureDefaultEnvironment(
        application: SpringApplication,
        standartEnvironment: StandardEnvironment,
        configure: (environment: StandardEnvironment) -> Unit = {},
    ): StandardEnvironment = standartEnvironment.apply {
        propertySources.addLast(ResourcePropertySource("classpath:application.properties"))
        configure(this)
        application.setEnvironment(this)
    }
}
