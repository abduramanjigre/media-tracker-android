package edu.metrostate.ics342.mediatracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val code: String,
    val message: String
)

class MediaNotFoundException(message: String) : Exception(message)

/** Thrown when POST /favorites returns 409 — the item is already favorited. */
class DuplicateFavoriteException : Exception("Already favorited")
