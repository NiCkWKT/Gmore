package edu.cuhk.csci3310.gmore.data.api.model

data class NewsDetailApiResult(
    val data: NewsData,
    val message: String,
    val success: Boolean
)
