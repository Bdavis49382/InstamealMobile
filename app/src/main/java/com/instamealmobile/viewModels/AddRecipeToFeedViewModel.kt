package com.instamealmobile.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddRecipeToFeedViewModel @Inject constructor(private val apiService: FeedService): ViewModel() {
    var ingredients = mutableStateListOf<String>()
    var title by  mutableStateOf("")
    var source by mutableStateOf("")
    var newIngredient by mutableStateOf("")
    var newStep by mutableStateOf("")
    private val _img_link = MutableLiveData<ApiState<String>>(null)
    val img_link: LiveData<ApiState<String>> = _img_link
    var steps = mutableStateListOf<String>()
    val householdId = "3hPKx3PwkPkPPlCVs53q"


    fun submitRecipe(confirm: (Recipe) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.addRecipe(householdId, "OKmkTNVx4TR6D6u9BjMJ", Recipe(
                    ingredients = ingredients,
                    title = title,
                    src_name = source,
                    img_link = if (img_link.value is ApiState.Success) {
                        (img_link.value as? ApiState.Success)?.data
                    } else "",
                    instructions = steps
                ))
                confirm(Recipe(title="",id=response))
            } catch (e: Exception) {
                // TODO: Add logging so something can be logged here
            }
        }
    }

    fun uploadImage(uri: Uri, context: Context) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)

            val requestBody = inputStream?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
            val file = File(uri.path)
            val multipartRequest = MultipartBody.Part.createFormData("file", file.name, requestBody!!)
            viewModelScope.launch {
                try {
                    _img_link.value = ApiState.Loading
                    val response = apiService.uploadImage(multipartRequest)
                    _img_link.value = ApiState.Success(response)
                } catch (e: Exception) {
                    _img_link.value = ApiState.Error("Failed to fetch data: ${e.message}")
                }
            }

        } catch (e: Exception) {
            Log.e("FILE_UPLOAD","file wasn't able to be processed or uploaded: ${e.message} ${e.stackTrace}")
        }
    }

}