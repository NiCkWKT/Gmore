package edu.cuhk.csci3310.gmore.data.api.model

data class OcrApiResult (
    val data: List<String>,
    val message: String,
    val success: Boolean
)