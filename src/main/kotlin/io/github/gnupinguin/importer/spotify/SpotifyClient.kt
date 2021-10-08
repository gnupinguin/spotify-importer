package io.github.gnupinguin.importer.spotify

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam


@FeignClient(name = "SpotifyClient",
    url = "https://api.spotify.com/v1",
    configuration = [SpotifyClientConfiguration::class])
interface SpotifyClient {

    @GetMapping(path = ["search"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun search(@RequestParam("q") q: String,
               @RequestParam("type") type: String = "track") : SpotifyTracksResponse

    @PutMapping(path = ["me/tracks"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addToLiked(@RequestParam("ids") ids: List<String>)

    @GetMapping(path = ["me/tracks"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun likedSongs(@RequestParam("offset") offset: Int = 0,
                   @RequestParam("limit") limit: Int = 50): PagingObject<SavedTrackObject>

}