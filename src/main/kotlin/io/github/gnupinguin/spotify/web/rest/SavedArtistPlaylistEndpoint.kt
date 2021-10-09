package io.github.gnupinguin.spotify.web.rest

import io.github.gnupinguin.spotify.client.SpotifyService
import io.github.gnupinguin.spotify.client.TrackObject
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/saved-artists")
class SavedArtistPlaylistEndpoint(private val spotifyService: SpotifyService) {

    @GetMapping
    fun getSaved(): List<Pair<String, String>> {
        return spotifyService.getSavedTracks()
            .flatMap { track -> track.artists }
            .distinct()
            .map { it.name to it.id }
    }

    @PostMapping("/{artistId}/playlist")
    fun createPlaylist(@PathVariable artistId: String): List<TrackObject> {
        return spotifyService.createSavedArtistPlaylist(artistId)
    }

}