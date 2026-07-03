package edu.metrostate.ics342.mediatracker.data.network


import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.SessionRepository
import edu.metrostate.ics342.mediatracker.data.model.Media

data class MediaPage(
    val items: List<Media>,
    val nextCursor: String?,
    val hasMore: Boolean
)

class DefaultMediaRepository(sessionRepository: SessionRepository) {

    private val api = RetrofitInstance.mediaApiService(sessionRepository)

    suspend fun search(query: String, type: String?, after: String?): MediaPage {
        val fakeResults = FakeMediaRepository.mediaList.filter {
            (type == null || it.mediaType == type) &&
            (query.isBlank() || it.title.contains(query, ignoreCase = true))
        }

        return try {
            val response = api.searchMedia(
                query = query.ifBlank { null },
                type  = type?.ifBlank { null },
                after = after
            )
            val items      = response.body() ?: emptyList()
            val nextCursor = response.headers()["X-Next-Cursor"]
            val hasMore    = response.headers()["X-Has-More"] == "true"
            
            // Combine real results with fake results for testing
            MediaPage(fakeResults + items, nextCursor, hasMore)
        } catch (e: Exception) {
            // If API fails, just return fake results
            MediaPage(fakeResults, null, false)
        }
    }
}


