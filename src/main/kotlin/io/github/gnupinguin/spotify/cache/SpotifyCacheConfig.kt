package io.github.gnupinguin.spotify.cache;

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.gnupinguin.spotify.service.SpotifySecurityContext
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration;
import java.lang.reflect.Method
import java.util.concurrent.TimeUnit

@Configuration
class SpotifyCacheConfig(private val cacheConfiguration: CacheConfiguration): CachingConfigurerSupport() {

    companion object {
        const val SPOTIFY_CACHE_MANAGER = "spotifyCacheManager"
        const val SPOTIFY_KEY_GENERATOR = "spotifyKeyGenerator"
    }

    @Bean(SPOTIFY_CACHE_MANAGER)
    fun savedTracksCacheManager(): CacheManager {
        val caffeine: Caffeine<Any, Any> = Caffeine.newBuilder()
            .maximumSize(cacheConfiguration.maxSize)
            .expireAfterWrite(cacheConfiguration.ttl, TimeUnit.SECONDS)
        val caffeineCacheManager = CaffeineCacheManager(CacheName.SAVED_TRACKS)
        caffeineCacheManager.setCaffeine(caffeine)
        caffeineCacheManager.isAllowNullValues = false
        return caffeineCacheManager
    }

    @Bean(SPOTIFY_KEY_GENERATOR)
    fun userIdKeyGenerator(context: SpotifySecurityContext) = UserIdKeyGenerator(context)

}

class UserIdKeyGenerator(private val context: SpotifySecurityContext) : KeyGenerator {
    override fun generate(target: Any, method: Method, vararg params: Any?): Any {
        return UserIdKey(target, method, params.asList(), context.getUserId())
    }
}

data class UserIdKey(val target: Any,
                     val method: Method,
                     val params: List<Any?>,
                     val userId: String)

class CacheName {
    companion object {
        const val SAVED_TRACKS = "savedTracksCache"
    }
}
