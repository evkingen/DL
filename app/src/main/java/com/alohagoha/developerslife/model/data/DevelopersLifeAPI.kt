package com.alohagoha.developerslife.model.data

import com.alohagoha.developerslife.model.data.dto.ResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DevelopersLifeAPI {

    @GET("{category}/{page}")
    suspend fun getGifs(
            @Path("category") category: String,
            @Path("page") page: Int,
            @Query("json") isJson: Boolean
    ): ResponseDTO?
}
