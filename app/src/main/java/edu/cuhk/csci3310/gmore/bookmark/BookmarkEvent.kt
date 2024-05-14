package edu.cuhk.csci3310.gmore.bookmark

sealed class BookmarkEvent {
    object onBackClick: BookmarkEvent()
}