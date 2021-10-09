package io.github.gnupinguin.spotify.client

import io.github.gnupinguin.spotify.cache.CacheName
import io.github.gnupinguin.spotify.cache.SpotifyCacheConfig.Companion.SPOTIFY_CACHE_MANAGER
import io.github.gnupinguin.spotify.cache.SpotifyCacheConfig.Companion.SPOTIFY_KEY_GENERATOR
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@CacheConfig(cacheManager = SPOTIFY_CACHE_MANAGER, keyGenerator = SPOTIFY_KEY_GENERATOR)
@FeignClient(name = "SpotifyClient",
    url = "https://api.spotify.com/v1",
    configuration = [SpotifyClientConfiguration::class])
interface SpotifyClient {

    @GetMapping(path = ["search"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun search(@RequestParam("q") q: String,
               @RequestParam("type") type: String = "track") : SpotifyTracksResponse

    @PutMapping(path = ["me/tracks"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addToLiked(@RequestParam("ids") ids: List<String>)

    @Cacheable(cacheNames = [CacheName.SAVED_TRACKS])
    @GetMapping(path = ["me/tracks"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun likedSongs(@RequestParam("offset") offset: Int = 0,
                   @RequestParam("limit") limit: Int = PagingUtils.MAX_PAGING_STEP
    ): PagingObject<SavedTrackObject>


    @PostMapping("users/{userId}/playlists", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createPlayList(@PathVariable userId: String, @RequestBody request: CreatePlaylistRequest): PlaylistObject

    @PostMapping("playlists/{playlistId}/tracks", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addToPlaylist(@PathVariable playlistId: String, @RequestBody request: AddItemsToPlaylistRequest): SnapshotObject
}