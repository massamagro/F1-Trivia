package com.vozmediano.f1trivia.data.network.api

import com.vozmediano.f1trivia.data.network.model.WikiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface WikiService {
    @GET("page/media-list/{title}")
    suspend fun getAllImages(
        @Path("title") title: String
    ): WikiResponse
}