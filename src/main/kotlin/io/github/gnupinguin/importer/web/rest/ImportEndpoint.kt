package io.github.gnupinguin.importer.web.rest

import io.github.gnupinguin.importer.client.SpotifyService
import io.github.gnupinguin.importer.client.UserTracksLoader
import io.github.gnupinguin.importer.spotify.SpotifyTrack
import io.github.gnupinguin.importer.spotify.TrackObject
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@CrossOrigin
@RestController
@RequestMapping("/api")
class ImportEndpoint(private val userTracksLoader: UserTracksLoader,
                     private val spotifyService: SpotifyService) {

    @PostMapping(value = ["import"])
    fun importFromFile(@RequestParam("file") file: MultipartFile) {
        val userTracks = userTracksLoader.loadTracks(file)
        val spotifyTracks = spotifyService.searchTracks(userTracks)
        spotifyService.addToSpotify(spotifyTracks)
    }

    //TODO unlogin periodically happens
    @PostMapping("search", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchSongs(@RequestParam("file") file: MultipartFile): List<SpotifyTrack> {
        val userTracks = userTracksLoader.loadTracks(file)
        return spotifyService.searchTracks(userTracks)
    }

    @GetMapping("user")
    fun getUser(@AuthenticationPrincipal principal: OAuth2User): Map<String, String> {
        return mapOf(
            "name" to principal.attributes["display_name"].toString()
        )
    }

    @GetMapping("saved-artists")
    fun getSaved(): Map<String, List<TrackObject>> {
        return spotifyService.getSavedTracks()
            .flatMap { track -> track.artists.map { artist -> artist.name to track } }
            .groupBy({ p -> p.first }, { p -> p.second }).toMap()
    }

}