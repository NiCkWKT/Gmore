package edu.cuhk.csci3310.gmore.news_detail

sealed class NewsDetailEvent {
    object onSaveNewsClick: NewsDetailEvent()
}