package edu.cuhk.csci3310.gmore.data.api

import edu.cuhk.csci3310.gmore.data.api.model.ApiResult
import edu.cuhk.csci3310.gmore.data.api.model.OcrApiResult
import edu.cuhk.csci3310.gmore.data.api.model.OcrEntity
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface NewsApi {

    @GET(ApiContstants.END_POINTS)
    suspend fun getNews(@Query("category") cat: String): ApiResult

    @Multipart
    @POST(ApiContstants.OCR)
    suspend fun postImgOcr(@Part image: MultipartBody.Part): OcrApiResult
}