package edu.cuhk.csci3310.gmore.presentation.news

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

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepo: NewsRepo
):ViewModel() {
    private val _newsApiResult = MutableLiveData<ApiResult>()
    val newsApiResult:LiveData<ApiResult>
        get() = _newsApiResult

    init {
        viewModelScope.launch {
            val newsApiResult = newsRepo.getNews()
            _newsApiResult.value = newsApiResult
        }
    }
}