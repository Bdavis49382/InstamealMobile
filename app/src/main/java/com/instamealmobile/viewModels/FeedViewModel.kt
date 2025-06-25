package com.instamealmobile.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.FeedService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val apiService: FeedService): ViewModel() {
    private val _feed = MutableStateFlow<ApiState<List<Recipe>>>(ApiState.Loading)
    val feed: MutableStateFlow<ApiState<List<Recipe>>> = _feed
    var isRefreshing by mutableStateOf(false)
    var scope = viewModelScope

    fun fetchFeed() {
        scope.launch {
            try {
                _feed.value = ApiState.Loading
                val response = apiService.getFeed()
                _feed.value = ApiState.Success(response)
            } catch (e: Exception) {
                _feed.value = ApiState.Error("Failed to fetch data: ${e.message}")
            } finally {
                isRefreshing = false
            }
        }
    }
    fun refreshFeed() {
        isRefreshing = true
        fetchFeed()
    }

    fun searchFeed(query: String) {
        _feed.value = ApiState.Loading
        scope.launch {
            try {
                val response = apiService.searchFeed(query)
                _feed.value = ApiState.Success(response)
            } catch (e: Exception) {
                _feed.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }
}
