package com.instamealmobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.network.FeedService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBarViewModel @Inject constructor(private val apiService: FeedService): ViewModel() {
    private val _tags = MutableStateFlow<ApiState<List<String>>>(ApiState.Loading)
    val tags = _tags
    var scope = viewModelScope

    fun getTags() {
        _tags.value = ApiState.Loading
        scope.launch {
            try {

                val response = apiService.getTags()
                _tags.value = ApiState.Success(response)
            } catch (e: Exception) {
                _tags.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

}
