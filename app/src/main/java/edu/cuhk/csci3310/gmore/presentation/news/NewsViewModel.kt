package edu.cuhk.csci3310.gmore.presentation.news

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.gmore.data.api.model.ApiResult
import edu.cuhk.csci3310.gmore.data.api.model.NewsData
import edu.cuhk.csci3310.gmore.data.repository.NewsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val _news = MutableStateFlow(emptyList<NewsData>())
    val news: StateFlow<List<NewsData>>
        get() = _news

    private var _newsUiState: NewsUiState by mutableStateOf(NewsUiState.Empty)
    val newsUiState: NewsUiState
        get() = _newsUiState

    fun getNews(category: String = "tech") {
        viewModelScope.launch {
            _newsUiState = NewsUiState.Loading
            _newsUiState = try {
                val newsApiResult = newsRepo.getNews(category)
                if (newsApiResult.success) {
                    _news.value = newsApiResult.data
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
}