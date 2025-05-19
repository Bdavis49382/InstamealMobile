package com.instamealmobile.viewModels

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
    val householdId = "3hPKx3PwkPkPPlCVs53q"

    fun fetchFeed() {
        viewModelScope.launch {
            try {
                val response = apiService.getFeed(householdId)
                _feed.value = ApiState.Success(response)
            } catch (e: Exception) {
                _feed.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

}
