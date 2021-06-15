package io.github.gnupinguin.importer.client

import io.github.gnupinguin.importer.spotify.SpotifyClient
import io.github.gnupinguin.importer.spotify.SpotifyTrack
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

interface SpotifyMigrator {

    fun searchTracks(tracks: List<UserTrack>) : List<SpotifyTrack>

    fun addToSpotify(tracks: List<SpotifyTrack>)

}

@Component
class SpotifyMigratorImpl(private val spotifyClient: SpotifyClient) : SpotifyMigrator {

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

    private fun createSearchQuery(userTrack: UserTrack): String =
        "artist:\"${userTrack.band.trim()}\" track:\"${userTrack.song.trim()}\""

}