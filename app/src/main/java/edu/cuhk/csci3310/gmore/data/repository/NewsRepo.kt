package edu.cuhk.csci3310.gmore.data.repository

import edu.cuhk.csci3310.gmore.data.api.NewsApi
import edu.cuhk.csci3310.gmore.data.api.model.ApiResult
import edu.cuhk.csci3310.gmore.data.api.model.NewsData
import edu.cuhk.csci3310.gmore.data.api.model.OcrApiResult
import edu.cuhk.csci3310.gmore.data.api.model.OcrEntity
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

class NewsRepo @Inject constructor(
    private val newsApi: NewsApi
){
    suspend fun getNews(category: String): ApiResult {
        return newsApi.getNews(category)
    }

    suspend fun postImgOcr(image: MultipartBody.Part): OcrApiResult {
        return newsApi.postImgOcr(image)
    }
}