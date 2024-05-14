package edu.cuhk.csci3310.gmore.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarkedNews(
    val title: String,
    val publishedDate: String,
    val imageUrl: String,
    val summary1: String,
    val summary2: String,
    val summary3: String,
    val source: String,
    val sourceUrl: String,
    var isSaved: Boolean = false,

    @PrimaryKey
    val id: String
)