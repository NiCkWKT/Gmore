package edu.cuhk.csci3310.gmore.data.repository

import edu.cuhk.csci3310.gmore.data.api.NewsApi
import edu.cuhk.csci3310.gmore.data.api.model.ApiResult
import edu.cuhk.csci3310.gmore.data.api.model.NewsData
import edu.cuhk.csci3310.gmore.data.api.model.OcrApiResult
import edu.cuhk.csci3310.gmore.data.api.model.OcrEntity
import java.io.File
import javax.inject.Inject

class NewsRepo @Inject constructor(
    private val newsApi: NewsApi
){
    suspend fun getNews(): ApiResult {
        return newsApi.getNews("tech")
    }

    suspend fun postImgOcr(image: OcrEntity): OcrApiResult {
        return newsApi.postImgOcr(image)
    }
}