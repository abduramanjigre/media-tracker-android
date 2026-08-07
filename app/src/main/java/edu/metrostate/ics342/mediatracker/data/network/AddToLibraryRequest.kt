package edu.metrostate.ics342.mediatracker.data.network

import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatusSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToLibraryRequest(
    @SerialName("media_id")
    val mediaId: Int,
    @SerialName("status")
    @Serializable(with = LibraryStatusSerializer::class)
    val status: LibraryStatus
)
