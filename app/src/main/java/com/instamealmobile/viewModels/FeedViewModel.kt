package com.instamealmobile.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.FeedPagingSource
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
    val pagingFlow = Pager(
        config = PagingConfig(pageSize = 50),
        pagingSourceFactory = { FeedPagingSource(apiService)}
    ).flow.cachedIn(viewModelScope)

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
