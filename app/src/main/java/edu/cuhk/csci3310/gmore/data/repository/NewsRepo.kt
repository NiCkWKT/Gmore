package edu.cuhk.csci3310.gmore.data.repository

import edu.cuhk.csci3310.gmore.data.api.NewsApi
import edu.cuhk.csci3310.gmore.data.api.model.NewsDetailApiResult
import edu.cuhk.csci3310.gmore.data.api.model.NewsListApiResult
import edu.cuhk.csci3310.gmore.data.api.model.OcrApiResult
import okhttp3.MultipartBody
import javax.inject.Inject

class NewsRepo @Inject constructor(
    private val newsApi: NewsApi
){
    suspend fun getNews(category: String): NewsListApiResult {
        return newsApi.getNews(category)
    }

    suspend fun getNewsById(id: String): NewsDetailApiResult {
        return newsApi.getNewsById(id)
    }

    suspend fun postImgOcr(image: MultipartBody.Part): OcrApiResult {
        return newsApi.postImgOcr(image)
    }
}