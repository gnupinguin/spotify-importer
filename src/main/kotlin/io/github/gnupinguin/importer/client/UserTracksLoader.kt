package io.github.gnupinguin.importer.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import java.io.File

interface UserTracksLoader {

    fun loadTracks(file: ByteArray): List<UserTrack>

}

@Component
class UserTracksLoaderImpl(private val objectMapper: ObjectMapper) : UserTracksLoader {

    override fun loadTracks(file: ByteArray): List<UserTrack> {
        return objectMapper.readValue(file)
    }

}