package io.github.gnupinguin.spotify.client

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SpotifyTrack(val id: String,
                        val name: String)

data class SpotifyTracks(val items: List<SpotifyTrack>)

data class SpotifyTracksResponse(val tracks: SpotifyTracks)

data class PagingObject<T>(val href: String,
                           val items: List<T>,
                           val limit: Int,
                           val next: String?,
                           val offset: Int,
                           val previous: String?,
                           val total: Int)

data class SavedTrackObject(@JsonProperty("added_at") val addedAt: Date,
                            val track: TrackObject
)

data class TrackObject(val album: SimplifiedAlbumObject,
                       val artists: List<ArtistObject>,
                       val id: String,
                       val name: String)

data class SimplifiedAlbumObject(val id: String,
                                 val name: String)

data class ArtistObject(val id: String,
                        val name: String)

data class CreatePlaylistRequest(val name: String, val description: String?)

data class PlaylistObject(val id: String, val name: String, val description: String?)

data class AddItemsToPlaylistRequest(val uris: List<String>, val position: Int? = null)

data class SnapshotObject(@JsonProperty("snapshot_id") val snapshotId: String)
