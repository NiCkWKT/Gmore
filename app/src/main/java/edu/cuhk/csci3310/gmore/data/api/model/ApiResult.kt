package edu.cuhk.csci3310.gmore.data.api.model

data class ApiResult(
    val data: List<NewsData>,
    val message: String,
    val success: Boolean
)