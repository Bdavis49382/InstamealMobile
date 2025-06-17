package com.instamealmobile.data

sealed class ApiState<out T> {
    data object Loading : ApiState<Nothing>()
    data object Resting : ApiState<Nothing>()
    data class Success<T>(val data: T) : ApiState<T>()
    data class Error(val message: String) : ApiState<Nothing>()
}

enum class ScreenState {
    Loading, Success, Error, Resting
}