package edu.metrostate.ics342.mediatracker.data.network

import edu.metrostate.ics342.mediatracker.data.model.Media
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaApiService {
    @GET("media")
    suspend fun searchMedia(
        @Query("query") query: String?,
        @Query("type") type: String?,
        @Query("after") after: String?
    ): Response<List<Media>>
}
