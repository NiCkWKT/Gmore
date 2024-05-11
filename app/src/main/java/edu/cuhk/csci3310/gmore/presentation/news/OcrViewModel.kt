package edu.cuhk.csci3310.gmore.presentation.news

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.gmore.data.api.model.OcrEntity
import edu.cuhk.csci3310.gmore.data.repository.NewsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

sealed interface OcrUiState {
    data class Success(val summary: List<String>): OcrUiState
    object Loading: OcrUiState
    object Error: OcrUiState
    object Empty: OcrUiState
}

@HiltViewModel
class OcrViewModel @Inject constructor(
    private val newsRepo: NewsRepo
): ViewModel() {
    private val _ocrSummaries = MutableStateFlow(emptyList<String>())
    val ocrSummaries: StateFlow<List<String>>
        get() = _ocrSummaries
    private var _ocrUiState: OcrUiState by mutableStateOf(OcrUiState.Empty)
    val ocrUiState: OcrUiState
        get() = _ocrUiState


    public fun getSummaries(file: File) {
        viewModelScope.launch {
            _ocrUiState = OcrUiState.Loading

            _ocrUiState = try {
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

                val apiResult = newsRepo.postImgOcr(imagePart)
                if (apiResult.success) {
                    _ocrSummaries.value = apiResult.data
                    Log.d("Gmore", apiResult.data.toString())
                    OcrUiState.Success(apiResult.data)
                } else {
                    throw Exception(apiResult.message)
                }

            } catch (e: Exception){
                Log.d("Gmore", e.toString())
                OcrUiState.Error
            }
        }

    }

    public fun clear() {
        viewModelScope.launch {
            _ocrUiState = OcrUiState.Empty
        }
    }

    init {

    }
}