package dev.niessen.webhookservice.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler

@Configuration
class BeanConfiguration {

    @Bean("jsonObjectMapper")
    @Primary
    fun jsonObjectMapper(): ObjectMapper = ObjectMapper()
        .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Bean
    @Primary
    fun httpClientErrorHandler() = object : DefaultResponseErrorHandler() {
        override fun hasError(response: ClientHttpResponse) = false
    }

}
