package io.github.gnupinguin.spotify.service

import io.github.gnupinguin.spotify.client.*
import io.github.gnupinguin.spotify.service.PagingUtils.Companion.MAX_PAGING_STEP
import io.github.gnupinguin.spotify.service.importer.UserTrack
import org.springframework.stereotype.Component

interface SpotifyService {

    fun searchTracks(tracks: List<UserTrack>) : List<SpotifyTrack>

    fun addToSpotify(tracks: List<SpotifyTrack>)

    fun getSavedTracks(): List<TrackObject>

    fun createSavedArtistPlaylist(artistId: String): List<TrackObject>

}

@Component
class SpotifyServiceImpl(private val spotifyClient: SpotifyClient,
                         private val securityContext: SpotifySecurityContext
) : SpotifyService {

    override fun addToSpotify(tracks: List<SpotifyTrack>) {
        val ids = tracks.map { it.id }
        spotifyClient.addToLiked(ids)
    }

    override fun searchTracks(tracks: List<UserTrack>): List<SpotifyTrack> {
        return tracks.mapNotNull { userTrack ->
            val query = createSearchQuery(userTrack)
            val search = spotifyClient.search(q = query)
            if (search.tracks.items.isNotEmpty()) {
                search.tracks.items[0] //TODO show all results
            } else {
                null
            }
        }
    }

    override fun getSavedTracks(): List<TrackObject> {
        return loadLikedSongs()
            .flatMap { it.items }
            .map { it.track }
            .toList()
    }

    override fun createSavedArtistPlaylist(artistId: String): List<TrackObject> {
        val tracks = getSavedTracks().filter { track -> isArtistsTrack(track, artistId) }
        if (tracks.isNotEmpty()) {
            val artistName = getArtistName(tracks, artistId)
            val request = CreatePlaylistRequest("${artistName}: @SpotifyUtilsTelegramBot", "The playlist was created by @SpotifyUtilsTelegramBot from your liked songs for specified artist.")
            val playlist = spotifyClient.createPlayList(securityContext.getUserId(), request)

            tracks.map { "spotify:track:${it.id}" }
                .windowed(100, 100, true)
                .forEach {
                    spotifyClient.addToPlaylist(playlist.id, AddItemsToPlaylistRequest(uris = it))
                }
            return tracks
        }
        return emptyList()
    }

    private fun getArtistName(
        tracks: List<TrackObject>,
        artistId: String
    ) = tracks.first().artists.find { it.id == artistId }!!.name

    private fun isArtistsTrack(
        track: TrackObject,
        artistId: String
    ) = track.artists.map { it.id }.contains(artistId)

    private fun loadLikedSongs() = generateSequence(spotifyClient.likedSongs()) {
        if (it.next != null) {
            spotifyClient.likedSongs(offset = it.offset + MAX_PAGING_STEP)
        } else {
            null
        }
    }

    private fun createSearchQuery(userTrack: UserTrack): String =
        "artist:\"${userTrack.band.trim()}\" track:\"${userTrack.song.trim()}\""

}