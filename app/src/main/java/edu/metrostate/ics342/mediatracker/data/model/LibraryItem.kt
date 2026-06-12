package edu.metrostate.ics342.mediatracker.data.model

import androidx.annotation.StringRes
import edu.metrostate.ics342.mediatracker.R

data class LibraryItem(
    val userId: String,
    val mediaId: Int,
    val status: LibraryStatus,
    val addedAt: String,
    val updatedAt: String,
    val media: Media
)

enum class LibraryStatus(@param:StringRes val labelRes: Int) {
    WANT_TO(R.string.status_want_to),
    IN_PROGRESS(R.string.status_in_progress),
    FINISHED(R.string.status_finished);

    fun toApiString(): String = when (this) {
        WANT_TO     -> "want_to"
        IN_PROGRESS -> "in_progress"
        FINISHED    -> "finished"
    }

    companion object {
        fun fromString(value: String): LibraryStatus = when (value) {
            "want_to"     -> WANT_TO
            "in_progress" -> IN_PROGRESS
            "finished"    -> FINISHED
            else          -> WANT_TO
        }
    }
}
