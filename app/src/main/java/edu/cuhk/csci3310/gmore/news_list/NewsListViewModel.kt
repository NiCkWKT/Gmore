package edu.cuhk.csci3310.gmore.news_list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.gmore.ScreenRoute
import edu.cuhk.csci3310.gmore.data.api.model.NewsData
import edu.cuhk.csci3310.gmore.data.repository.NewsRepo
import edu.cuhk.csci3310.gmore.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface NewsUiState {
    data class Success(val news: List<NewsData>): NewsUiState
    object Loading: NewsUiState
    object Error: NewsUiState
    object Empty: NewsUiState
}

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepo: NewsRepo
):ViewModel() {
    private val TAG = "Gmore"

    private val _news = MutableStateFlow(emptyList<NewsData>())
    val news: StateFlow<List<NewsData>>
        get() = _news

    private var _newsUiState: NewsUiState by mutableStateOf(NewsUiState.Empty)
    val newsUiState: NewsUiState
        get() = _newsUiState


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: NewsListEvent) {
        when(event) {
            is NewsListEvent.onNewsClick -> {
                Log.d(TAG, "NewsViewModel onEvent: ${event.news.id}")
                sendUiEvent(UiEvent.Navigate(ScreenRoute.NewsDetail.route + "?newsId=${event.news.id}"))
//                val route = ScreenRoute.NewsDetail.route + "/${event.news.id}"
//                Log.d(TAG, "NewsViewModel onEvent route: $route")
//                sendUiEvent(UiEvent.Navigate(ScreenRoute.NewsDetail.route + "/${event.news.id}"))
            }
        }
    }

    fun getNews(category: String = "general") {
        viewModelScope.launch {
            _newsUiState = NewsUiState.Loading
            _newsUiState = try {
                val newsApiResult = newsRepo.getNews(category)
                if (newsApiResult.success) {
                    _news.value = newsApiResult.data
                    Log.d(TAG, "NewsViewModel getNews: ${newsApiResult.data}")
                    NewsUiState.Success(newsApiResult.data)
                } else {
                    throw Exception(newsApiResult.message)
                }
            } catch (e: Exception) {
                Log.d("Gmore", e.toString())
                NewsUiState.Error
            }
        }
    }

    init {
        getNews()
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}