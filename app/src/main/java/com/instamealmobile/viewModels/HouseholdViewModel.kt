package com.instamealmobile.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val userId = "OKmkTNVx4TR6D6u9BjMJ"
    private val _householdId = MutableLiveData<ApiState<String>>(ApiState.Loading)
    val householdId: LiveData<ApiState<String>> = _householdId
    private val _code = MutableLiveData<ApiState<String>>(ApiState.Loading)
    val code: LiveData<ApiState<String>> = _code
    var codeEntry by mutableStateOf("")

    fun getId() {
        viewModelScope.launch {
            try {
                val response = apiService.getHouseholdId(userId)
                _householdId.value = ApiState.Success(response.household_id)
            } catch (e: Exception) {
                _householdId.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }
    fun getUsers(householdId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getHousehold(householdId)
                _users.value = ApiState.Success(response)
            } catch (e: Exception) {
                _users.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun getCode() {
        viewModelScope.launch {
            try {
                val response = apiService.getHouseholdCode("3hPKx3PwkPkPPlCVs53q")
                _code.value = ApiState.Success(response)
            } catch (e: Exception) {
                _code.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun joinHousehold() {
        viewModelScope.launch {
            try {
                val response = apiService.joinHousehold("DIdcGPbP3Y2zJWS9sxqu",codeEntry)
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
                val response = apiService.kickUser("3hPKx3PwkPkPPlCVs53q", user_id)
                _users.value = ApiState.Success(response)
            } catch (e: Exception) {
                _users.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }

    }
}
