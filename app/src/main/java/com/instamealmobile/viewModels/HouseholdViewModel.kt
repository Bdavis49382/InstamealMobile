package com.instamealmobile.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.User
import com.instamealmobile.network.HouseholdService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseholdViewModel @Inject constructor(private val apiService: HouseholdService): ViewModel() {
    private val _users = MutableLiveData<ApiState<List<User>>>(ApiState.Loading)
    val users: LiveData<ApiState<List<User>>> = _users
    val user = Firebase.auth.currentUser
    private val _code = MutableLiveData<ApiState<String>>(ApiState.Loading)
    val code: LiveData<ApiState<String>> = _code
    var codeEntry by mutableStateOf("")

    fun getUsers() {
        viewModelScope.launch {
            try {
                val response = apiService.getHousehold()
                _users.value = ApiState.Success(response)
            } catch (e: Exception) {
                _users.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun getCode() {
        viewModelScope.launch {
            try {
                val response = apiService.getHouseholdCode()
                _code.value = ApiState.Success(response)
            } catch (e: Exception) {
                _code.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun joinHousehold() {
        viewModelScope.launch {
            try {
                val response = apiService.joinHousehold(user?.uid?:"",codeEntry)
                _users.value = ApiState.Success(response)
            } catch (e: Exception) {
                _users.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }

    }

    fun kickUser(user_id: String) {
        viewModelScope.launch {
            try {
                Log.i("Fun Fact","kicking user: $user_id")
                val response = apiService.kickUser(user?.uid?:"")
                _users.value = ApiState.Success(response)
            } catch (e: Exception) {
                _users.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }

    }
}
