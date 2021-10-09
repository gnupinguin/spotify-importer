package io.github.gnupinguin.spotify.cache;

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


@ConfigurationProperties(prefix = "spotify.client.cache")
data class CacheConfiguration @ConstructorBinding constructor(val maxSize: Long, val ttl: Long)
