package edu.metrostate.ics342.mediatracker.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToFavoritesRequest(
    @SerialName("media_id")
    val mediaId: Int
)
