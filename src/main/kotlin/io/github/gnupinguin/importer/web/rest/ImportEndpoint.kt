package io.github.gnupinguin.importer.web.rest

import io.github.gnupinguin.importer.client.SpotifyMigrator
import io.github.gnupinguin.importer.client.UserTracksLoader
import org.springframework.http.HttpRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController("/api")
class ImportEndpoint(private val userTracksLoader: UserTracksLoader,
                     private val spotifyMigrator: SpotifyMigrator
) {

    @PostMapping(value = ["import"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun importFromFile(@RequestParam("file") file: MultipartFile, request: HttpRequest) {
        val userTracks = userTracksLoader.loadTracks(file.bytes)
        val spotifyTracks = spotifyMigrator.searchTracks(userTracks)
        spotifyMigrator.addToSpotify(spotifyTracks)
    }

}