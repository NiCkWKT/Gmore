package edu.cuhk.csci3310.gmore.news_detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.gmore.data.api.model.NewsData
import edu.cuhk.csci3310.gmore.data.db.MarkedNews
import edu.cuhk.csci3310.gmore.data.db.MarkedNewsRepository
import edu.cuhk.csci3310.gmore.data.repository.NewsRepo
import edu.cuhk.csci3310.gmore.news_list.NewsUiState
import edu.cuhk.csci3310.gmore.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface NewsDetailUiState {
    object Success: NewsDetailUiState
    object Loading: NewsDetailUiState
    object Error: NewsDetailUiState
    object Empty: NewsDetailUiState
}

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val repository: NewsRepo,
    private val markedRepository: MarkedNewsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val TAG = "Gmore"
//    private val _news = MutableStateFlow()
    var news by mutableStateOf<NewsData?>(null)
        private set

//    val news: StateFlow<NewsData>
//        get() = _news

    private var _newsUiState: NewsDetailUiState by mutableStateOf(NewsDetailUiState.Empty)
    val newsUiState: NewsDetailUiState
        get() = _newsUiState

    var title by mutableStateOf("")
        private set

    var publishedDate by mutableStateOf("")
        private set

    var imageUrl by mutableStateOf("")
        private set

    var source by mutableStateOf("")
        private set

    var sourceUrl by mutableStateOf("")
        private set

    var point1 by mutableStateOf("")
        private set

    var point2 by mutableStateOf("")
        private set

    var point3 by mutableStateOf("")
        private set



    private val _uiEvent = Channel<UiEvent> {  }
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        Log.d(TAG, "NewsDetailViewModel init")
        val newsId = savedStateHandle.get<String>("newsId")!!
        Log.d(TAG, "newsId: $newsId")
        if(!newsId.equals("null")) {
            Log.d(TAG, "News ID not null")
            viewModelScope.launch {
                _newsUiState = NewsDetailUiState.Loading
                _newsUiState = try {
                    val newsApiResult = repository.getNewsById(newsId)
                    Log.d(TAG, "NewsDetailViewModel getNews: $newsApiResult")
                    if (newsApiResult.success) {
//                        _news.value = newsApiResult.data
                        title = newsApiResult.data.title
                        publishedDate = newsApiResult.data.published_at
                        imageUrl = newsApiResult.data.image_url
                        source = newsApiResult.data.source
                        sourceUrl = newsApiResult.data.source_url
                        point1 = newsApiResult.data.summary[0]
                        point2 = newsApiResult.data.summary[1]
                        point3 = newsApiResult.data.summary[2]

                        NewsDetailUiState.Success
                    } else {
                        throw Exception(newsApiResult.message)
                    }
                } catch (e: Exception) {
                    NewsDetailUiState.Error
                }
            }
        }
    }

    fun onEvent(event: NewsDetailEvent) {
        when(event) {
            is NewsDetailEvent.onSaveNewsClick -> {
                viewModelScope.launch {
                    val newsData = news
                    val markedNews = newsData?.let {
                        MarkedNews(
                            id = it.id,
                            title = newsData.title,
                            publishedDate = newsData.published_at,
                            imageUrl = newsData.image_url,
                            summary1 = newsData.summary[0],
                            summary2 = newsData.summary[1],
                            summary3 = newsData.summary[2],
                            source = newsData.source,
                            sourceUrl = newsData.source_url
                        )
                    }
                    if (markedNews != null) {
                        markedRepository.saveNews(markedNews)
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}