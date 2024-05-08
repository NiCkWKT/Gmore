package edu.cuhk.csci3310.gmore.data.api

import edu.cuhk.csci3310.gmore.data.api.model.ApiResult
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET(ApiContstants.END_POINTS)
    suspend fun getNews(@Query("category") cat: String): ApiResult
}