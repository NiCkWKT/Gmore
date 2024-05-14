package edu.cuhk.csci3310.gmore.news_detail

import edu.cuhk.csci3310.gmore.data.api.model.NewsData
import edu.cuhk.csci3310.gmore.data.db.MarkedNews

sealed class NewsDetailEvent {
    data class onSaveNewsClick(val news: MarkedNews, val isSaved: Boolean): NewsDetailEvent()
    object onBackClick: NewsDetailEvent()
}