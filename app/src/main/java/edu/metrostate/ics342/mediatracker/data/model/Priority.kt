package edu.metrostate.ics342.mediatracker.data.model

import edu.metrostate.ics342.mediatracker.data.network.PriorityRequest
import kotlinx.serialization.Serializable

@Serializable
data class Priority(
    val mediaId: Int,
    val priority: Int, // 1 = highest, 3 = lowest
    val orderIndex: Int,
    val estimatedTimeHours: Int? = null,
    val notes: String? = null,
    val media: Media? = null
)

fun Priority.toRequest() = PriorityRequest(
    mediaId = mediaId,
    priority = priority,
    orderIndex = orderIndex,
    estimatedTimeHours = estimatedTimeHours,
    notes = notes
)
