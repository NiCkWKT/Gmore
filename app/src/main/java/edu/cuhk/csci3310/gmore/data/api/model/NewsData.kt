package edu.cuhk.csci3310.gmore.data.api.model

data class NewsData(
    val id: String,
    val title: String,
    val published_at: String,
    val image_url: String,
    val source: String,
    val source_url: String,
    val category: String,
    val summary: List<String>,
)