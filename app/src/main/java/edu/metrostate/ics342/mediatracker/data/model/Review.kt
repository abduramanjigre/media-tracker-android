package edu.metrostate.ics342.mediatracker.data.model

data class Review(
    val userId: String,
    val mediaId: Int,
    val rating: Int,
    val reviewText: String? = null,
    val createdAt: String,
    val user: UserProfile? = null,
    val media: Media? = null
)


