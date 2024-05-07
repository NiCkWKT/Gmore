package edu.cuhk.csci3310.gmore.data.repository

import edu.cuhk.csci3310.gmore.data.api.NewsApi
import edu.cuhk.csci3310.gmore.data.api.model.ApiResult
import edu.cuhk.csci3310.gmore.data.api.model.NewsData
import javax.inject.Inject

class NewsRepo @Inject constructor(
    private val newsApi: NewsApi
){
    suspend fun getNews(): ApiResult {
        return newsApi.getNews("tech")
    }
}