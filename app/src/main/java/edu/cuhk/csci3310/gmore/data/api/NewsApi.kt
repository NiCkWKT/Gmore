package edu.cuhk.csci3310.gmore.data.api

import edu.cuhk.csci3310.gmore.data.api.model.NewsDetailApiResult
import edu.cuhk.csci3310.gmore.data.api.model.NewsListApiResult
import edu.cuhk.csci3310.gmore.data.api.model.OcrApiResult
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApi {

    @GET(ApiConstants.END_POINTS)
    suspend fun getNews(@Query("category") cat: String): NewsListApiResult


    @GET(ApiConstants.END_POINTS + "/{id}")
    suspend fun getNewsById(@Path("id") id: String): NewsDetailApiResult

    @Multipart
    @POST(ApiConstants.OCR)
    suspend fun postImgOcr(@Part image: MultipartBody.Part): OcrApiResult
}