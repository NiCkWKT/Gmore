package edu.cuhk.csci3310.gmore.news_list

import edu.cuhk.csci3310.gmore.data.api.model.NewsData

sealed class NewsListEvent {
    data class onNewsClick(val news: NewsData): NewsListEvent()
}