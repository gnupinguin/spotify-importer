package io.github.gnupinguin.importer.spotify

data class SpotifyTrack(
    val id: String,
    val name: String
)

data class SpotifyTracks(
    val items: List<SpotifyTrack>
)

data class SpotifyTracksResponse(
    val tracks: SpotifyTracks
)