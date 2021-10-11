package io.github.gnupinguin.spotify.client

import feign.*
import feign.codec.Decoder
import feign.codec.Encoder
import feign.httpclient.ApacheHttpClient
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import io.github.gnupinguin.spotify.service.SpotifySecurityContext
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import java.util.concurrent.TimeUnit

class SpotifyClientConfiguration(private val spotifySecurityContext: SpotifySecurityContext) {

    private fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }

    private fun encoder(): Encoder {
        return JacksonEncoder()
    }

    private fun decoder(): Decoder {
        return JacksonDecoder()
    }

    private fun authRequestInterceptor(): RequestInterceptor {
        return AuthorizationHeaderInterceptor { "Bearer ${spotifySecurityContext.getAccessToken()}" }
    }

    @Bean
    fun feignBuilder(clientService: OAuth2AuthorizedClientService): Feign.Builder {
        return Feign.builder()
            .retryer(Retryer.Default(100, 1, 0))
            .client(feignClient())
            .encoder(encoder())
            .decoder(decoder())
            .requestInterceptor(authRequestInterceptor())
            .logLevel(feignLoggerLevel())
    }

    private fun feignClient(): Client {
        val builder = HttpClientBuilder.create()
            .setMaxConnTotal(64)
            .setMaxConnPerRoute(64)
            .setConnectionTimeToLive(10, TimeUnit.SECONDS)
        return ApacheHttpClient(builder.build())
    }

}

class AuthorizationHeaderInterceptor(private val headerValueProvider: () -> String): RequestInterceptor {
    override fun apply(template: RequestTemplate?) {
        template?.header("Authorization", headerValueProvider.invoke())
    }
}
