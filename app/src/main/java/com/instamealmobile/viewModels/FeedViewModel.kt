package com.instamealmobile.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.FeedService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val apiService: FeedService): ViewModel() {
    private val _feed = MutableLiveData<ApiState<List<Recipe>>>(ApiState.Loading)
    val feed: LiveData<ApiState<List<Recipe>>> = _feed
    var isRefreshing by mutableStateOf(false)
    val householdId = "3hPKx3PwkPkPPlCVs53q"

    fun fetchFeed() {
        viewModelScope.launch {
            try {
                _feed.value = ApiState.Loading
                val response = apiService.getFeed(householdId)
                _feed.value = ApiState.Success(response)
                isRefreshing = false
            } catch (e: Exception) {
                _feed.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }
    fun refreshFeed() {
        isRefreshing = true
        fetchFeed()
    }

    fun searchFeed(query: String) {
        _feed.value = ApiState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.searchFeed(householdId, query)
                _feed.value = ApiState.Success(response)
            } catch (e: Exception) {
                _feed.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }
}
