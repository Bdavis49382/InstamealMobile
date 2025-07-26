package com.instamealmobile.network

import android.content.Context
import androidx.lifecycle.ViewModel
import coil.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @ApplicationContext private val appContext: Context
    ): ViewModel() {
    val imageLoader: ImageLoader by lazy {
        ImageLoader.Builder(appContext)
            .okHttpClient(okHttpClient)
            .build()
    }
}