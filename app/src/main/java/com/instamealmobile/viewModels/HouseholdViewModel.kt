package com.instamealmobile.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.User
import com.instamealmobile.network.HouseholdService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseholdViewModel @Inject constructor(private val apiService: HouseholdService): ViewModel() {
    private val _users = MutableStateFlow<ApiState<List<User>>>(ApiState.Loading)
    val users: MutableStateFlow<ApiState<List<User>>> = _users
    private val _code = MutableStateFlow<ApiState<String>>(ApiState.Loading)
    val code: MutableStateFlow<ApiState<String>> = _code
    var codeEntry by mutableStateOf("")
    var scope = viewModelScope

    fun getUsers() {
        _users.value = ApiState.Loading
        scope.launch {
            try {
                val response = apiService.getHousehold()
                _users.value = ApiState.Success(response)
            } catch (e: Exception) {
                _users.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun getCode() {
        scope.launch {
            try {
                val response = apiService.getHouseholdCode()
                _code.value = ApiState.Success(response)
            } catch (e: Exception) {
                _code.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun joinHousehold(reload: () -> Unit) {
        scope.launch {
            try {
                val response = apiService.joinHousehold(codeEntry)
                _users.value = ApiState.Success(response)
                reload()
            } catch (e: Exception) {
                _users.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }

    }

    fun kickUser(user_id: String) {
        scope.launch {
            try {
                Log.i("Fun Fact","kicking user: $user_id")
                val response = apiService.kickUser(user_id)
                _users.value = ApiState.Success(response)
            } catch (e: Exception) {
                _users.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }
}
