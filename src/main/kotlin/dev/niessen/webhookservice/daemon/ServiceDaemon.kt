package dev.niessen.webhookservice.daemon

import dev.niessen.webhookservice.DeploymentType
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Service

@Service
@EnableScheduling
class ServiceDaemon(
    private val taskScheduler: TaskScheduler,
    @Value("\${application.deployment.type}") private val deploymentType: DeploymentType,
    @Value("\${application.daemon.cron_expression}") private val cronExpression: String,

) {

    private val serviceDaemon = Runnable {
        println("SERVICE RUNNING")
    }

    @PostConstruct
    fun startScheduler() {
        if (deploymentType == DeploymentType.DAEMON) {
            taskScheduler.schedule(serviceDaemon, CronTrigger(cronExpression))
        }
    }
}
