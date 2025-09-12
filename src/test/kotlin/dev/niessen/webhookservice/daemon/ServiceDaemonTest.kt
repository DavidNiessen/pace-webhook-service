package dev.niessen.webhookservice.daemon

import dev.niessen.webhookservice.DeploymentType
import dev.niessen.webhookservice.service.PaceService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import java.util.concurrent.ScheduledFuture


@ExtendWith(MockitoExtension::class)
class ServiceDaemonTest {

    @Mock
    private lateinit var taskScheduler: TaskScheduler

    @Mock
    private lateinit var paceService: PaceService

    @Mock
    private lateinit var scheduledFuture: ScheduledFuture<*>

    @BeforeEach
    fun setup() {
        lenient().`when`(taskScheduler.schedule(any<Runnable>(), any<CronTrigger>())).thenAnswer {
            it.getArgument<Runnable>(0).run()
            scheduledFuture
        }
    }

    @Test
    fun `should start scheduler if app is running as service daemon`() {
        ServiceDaemon(DeploymentType.DAEMON, "0 0 6 * * MON-FRI", taskScheduler, paceService).startScheduler()

        verify(paceService, times(1)).getAndDispatchTodaysMenu()
    }

    @Test
    fun `should not start scheduler if app is not running as service daemon`() {
        ServiceDaemon(DeploymentType.CLOUD_FUNCTION, "0 0 6 * * MON-FRI", taskScheduler, paceService).startScheduler()

        verify(paceService, times(0)).getAndDispatchTodaysMenu()
    }

}
