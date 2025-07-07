package com.instamealmobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.instamealmobile.network.FeedPagingSource
import com.instamealmobile.network.FeedService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val apiService: FeedService): ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query
    var scope = viewModelScope
    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingFlow = query.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 50),
            pagingSourceFactory = { FeedPagingSource(apiService, query) }
        ).flow
    }.cachedIn(viewModelScope)

    fun searchFeed(query: String) {
        _query.value = query
    }
}
