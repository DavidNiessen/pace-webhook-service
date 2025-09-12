package dev.niessen.webhookservice

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.core.env.StandardEnvironment
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@SpringBootApplication
@ConfigurationPropertiesScan
class Application

fun main(args: Array<String>) {
    val application = SpringApplication(Application::class.java)

    DeploymentTypePreConfigurer.configureAppByDeploymentType(application, StandardEnvironment())

    application.run(*args)
}
