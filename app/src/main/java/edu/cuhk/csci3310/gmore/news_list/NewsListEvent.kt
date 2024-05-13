package edu.cuhk.csci3310.gmore.news_list

sealed class NewsListEvent {
    data class onNewsClick(val newsId: Int): NewsListEvent()
}