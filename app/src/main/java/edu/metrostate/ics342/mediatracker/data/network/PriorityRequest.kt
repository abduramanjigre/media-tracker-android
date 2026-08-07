package edu.metrostate.ics342.mediatracker.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriorityRequest(
    @SerialName("media_id")
    val mediaId: Int,
    @SerialName("priority")
    val priority: Int,
    @SerialName("order_index")
    val orderIndex: Int,
    @SerialName("estimated_time_hours")
    val estimatedTimeHours: Int? = null,
    @SerialName("notes")
    val notes: String? = null
)
