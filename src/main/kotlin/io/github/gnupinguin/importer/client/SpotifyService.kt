package io.github.gnupinguin.importer.client

import io.github.gnupinguin.importer.spotify.SpotifyClient
import io.github.gnupinguin.importer.spotify.SpotifyTrack
import io.github.gnupinguin.importer.spotify.TrackObject
import org.springframework.stereotype.Component

interface SpotifyService {

    fun searchTracks(tracks: List<UserTrack>) : List<SpotifyTrack>

    fun addToSpotify(tracks: List<SpotifyTrack>)

    fun getSavedTracks(): List<TrackObject>

}

@Component
class SpotifyServiceImpl(private val spotifyClient: SpotifyClient) : SpotifyService {

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

    //TODO add cache (should add user parameter or cache by oauth context)
    override fun getSavedTracks(): List<TrackObject> {
        return generateSequence(spotifyClient.likedSongs()) {
            if (it.next != null) {
                spotifyClient.likedSongs(it.offset + 50, 50)
            } else {
                null
            }
        }
            .flatMap { it.items }
            .map { it.track }
            .toList()
    }

    private fun createSearchQuery(userTrack: UserTrack): String =
        "artist:\"${userTrack.band.trim()}\" track:\"${userTrack.song.trim()}\""

}