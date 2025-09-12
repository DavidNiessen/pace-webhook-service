package dev.niessen.webhookservice

import dev.niessen.webhookservice.exception.exceptions.InvalidDeploymentTypeException
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.StandardEnvironment

@ExtendWith(MockitoExtension::class)
class DeploymentTypePreConfigurerTest {

    @Mock
    private lateinit var application: SpringApplication

    @Mock
    private lateinit var standardEnvironment: StandardEnvironment

    @Mock
    private lateinit var mutablePropertySources: MutablePropertySources

    private var webApplicationType: WebApplicationType? = null

    @Test
    fun `test CLOUD_FUNCTION deployment type`() {
        setupMockEnvironment(DeploymentType.CLOUD_FUNCTION.name)
        DeploymentTypePreConfigurer.configureAppByDeploymentType(application, standardEnvironment)

        assertThat(webApplicationType, `is`(WebApplicationType.NONE))
    }

    @Test
    fun `test DAEMON deployment type`() {
        setupMockEnvironment(DeploymentType.DAEMON.name)
        DeploymentTypePreConfigurer.configureAppByDeploymentType(application, standardEnvironment)

        assertThat(webApplicationType, `is`(WebApplicationType.NONE))
    }

    @Test
    fun `test REST deployment type`() {
        setupMockEnvironment(DeploymentType.REST.name)
        DeploymentTypePreConfigurer.configureAppByDeploymentType(application, standardEnvironment)

        assertThat(webApplicationType, `is`(WebApplicationType.SERVLET))
    }

    @Test
    fun `test invalid deployment type`() {
        setupMockEnvironment("INVALID_DEPLOYMENT_TYPE", false)

        assertThrows<InvalidDeploymentTypeException> {
            DeploymentTypePreConfigurer.configureAppByDeploymentType(application, standardEnvironment)
        }
    }

    @Test
    fun `should fail on TEST deployment type`() {
        setupMockEnvironment(DeploymentType.TEST.name, false)

        assertThrows<InvalidDeploymentTypeException> {
            DeploymentTypePreConfigurer.configureAppByDeploymentType(application, standardEnvironment)
        }
    }


    private fun setupMockEnvironment(deploymentType: String, isValidDeploymentType: Boolean = true) {
        `when`(standardEnvironment.getProperty(eq("application.deployment.type")))
            .thenAnswer { deploymentType }

        `when`(standardEnvironment.propertySources).thenAnswer { mutablePropertySources }

        if (!isValidDeploymentType) {
            return
        }

        doAnswer { invocation ->
            webApplicationType = invocation.getArgument(0)
            Unit
        }.`when`(application).webApplicationType = any()
    }
}
