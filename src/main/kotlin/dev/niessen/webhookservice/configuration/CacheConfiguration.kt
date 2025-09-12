package dev.niessen.webhookservice.configuration

import com.github.benmanes.caffeine.cache.Caffeine
import dev.niessen.webhookservice.properties.PaceProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class CacheConfiguration(
    private val paceProperties: PaceProperties,
) {

    @Bean
    fun paceCache(): Caffeine<Any, Any> {
        return Caffeine.newBuilder()
            .expireAfterWrite(paceProperties.cacheTtlMs, TimeUnit.MILLISECONDS)
            .maximumSize(1)
    }

    @Bean
    fun paceCacheManager(caffeine: Caffeine<Any, Any>) = CaffeineCacheManager("paceCache").apply {
        setCaffeine(caffeine)
    }

}
