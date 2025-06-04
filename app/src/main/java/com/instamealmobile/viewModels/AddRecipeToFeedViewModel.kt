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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.FeedService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddRecipeToFeedViewModel @Inject constructor(private val apiService: FeedService): ViewModel() {
    var ingredients = mutableStateListOf<String>()
    var title =  mutableStateOf("")
    var servings = mutableStateOf("")
    var totalTime = mutableStateOf("")
    var source by mutableStateOf("")
    var newIngredient by mutableStateOf("")
    var newStep by mutableStateOf("")
    private val _img_link = MutableLiveData<ApiState<String>>(ApiState.Resting)
    val img_link: LiveData<ApiState<String>> = _img_link
    var steps = mutableStateListOf<String>()
    var validatorsActive = mutableStateOf(false)
    val householdId = "3hPKx3PwkPkPPlCVs53q"

    fun setRecipe(recipe: Recipe) {
        if (recipe.id.isNullOrEmpty()) {
            ingredients.clear()
            title.value = ""
            servings.value = ""
            totalTime.value = ""
            source = ""
            _img_link.value = ApiState.Resting
            steps.clear()

        } else {
            ingredients.clear()
            ingredients.addAll(recipe.ingredients)
            title.value = recipe.title
            servings.value = recipe.servings?: ""
            totalTime.value = if (recipe.time_estimate.size > 0) recipe.time_estimate[0] else ""
            source = recipe.src_name?: ""
            _img_link.value = ApiState.Success(recipe.img_link?:"")
            steps.clear()
            steps.addAll(recipe.instructions)

        }
    }
    fun validateRecipe() : Boolean {
        return ingredients.isNotEmpty() &&
                title.value.isNotEmpty() &&
                steps.isNotEmpty()
    }

    fun submitRecipe(id: String?, confirm: (Recipe) -> Unit): Boolean {
        if (validateRecipe()) {
            viewModelScope.launch {
                try {
                    val response = if (id.isNullOrEmpty()) {
                        apiService.addRecipe(
                            householdId, "OKmkTNVx4TR6D6u9BjMJ", Recipe(
                                ingredients = ingredients,
                                title = title.value,
                                servings = servings.value,
                                time_estimate = if (totalTime.value.isNotEmpty()) listOf(totalTime.value) else listOf(),
                                src_name = source,
                                img_link = if (img_link.value is ApiState.Success) {
                                    (img_link.value as? ApiState.Success)?.data
                                } else "",
                                instructions = steps
                            )
                        )
                    } else {
                        apiService.updateRecipe(
                            "OKmkTNVx4TR6D6u9BjMJ", id, Recipe(
                                ingredients = ingredients,
                                title = title.value,
                                servings = servings.value,
                                time_estimate = if (totalTime.value.isNotEmpty()) listOf(totalTime.value) else listOf(),
                                src_name = source,
                                img_link = if (img_link.value is ApiState.Success) {
                                    (img_link.value as? ApiState.Success)?.data
                                } else "",
                                instructions = steps
                            )
                        )
                    }
                    confirm(Recipe(title = "", id = response))
                } catch (e: Exception) {
                    Log.e("RECIPE_SUBMISSION", e.message ?: "Issue with submitting a new recipe")
                }
            }
            return true
        } else {
            return false
        }
    }

    fun uploadImage(uri: Uri, context: Context, after: () -> Unit) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)

            val requestBody = inputStream?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
            val fileName = if (!uri.path.isNullOrEmpty()) File(uri.path).name else "Default_Image_Name"
            val multipartRequest = MultipartBody.Part.createFormData("file", fileName, requestBody!!)
            viewModelScope.launch {
                try {
                    _img_link.value = ApiState.Loading
                    val response = apiService.uploadImage(multipartRequest)
                    _img_link.value = ApiState.Success(response)
                    after()
                } catch (e: Exception) {
                    _img_link.value = ApiState.Error("Failed to fetch data: ${e.message}")
                }
            }

        } catch (e: Exception) {
            Log.e("FILE_UPLOAD","file wasn't able to be processed or uploaded: ${e.message} ${e.stackTrace}")
        }
    }

    fun parseText(uri: Uri, context: Context, onceFinished: (String) -> Unit ) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image: InputImage
        try {
            image = InputImage.fromFilePath(context, uri)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    textToRecipe(visionText)
                    onceFinished(visionText.text)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun textToRecipe(visionText: Text) {

        var isIngredients = false
        var isSteps = false
        var titleFound = false
        ingredients.clear()
        steps.clear()
        for (block in visionText.textBlocks) {
                val words = block.text.split(' ')
                if (!titleFound and words.all { it[0] == it.uppercase()[0] }) {
                    title.value= block.text
                    titleFound = true
                    Log.i("TEXT_TRIGGER","title")
                    continue
                }
                if (block.text.uppercase().contains("YIELD:")) {
                    continue
                }
                if (block.text.uppercase().contains("PREP:")) {
                    Log.i("TEXT_TRIGGER","other time")
                    continue
                }
                else if (block.text.uppercase().contains("COOK:")) {
                    Log.i("TEXT_TRIGGER","other time")
                    continue
                }
                else if (block.text.uppercase().contains("TOTAL:")) {
                    totalTime.value = block.text.uppercase().replace("TOTAL:","").lowercase()
                    Log.i("TEXT_TRIGGER","total time")
                    continue
                }
                else if (block.text.uppercase().contains("SERVINGS:")) {
                    try {
                        servings.value = block.text.uppercase().replace("SERVINGS:","").lowercase()
                    } catch (e: Exception){
                        Log.i("TEXT_WARNING",e.message?: "servings failed to convert to float")
                    }
                    Log.i("TEXT_TRIGGER","servings")
                    continue
                }
                if (block.text.uppercase() == "INGREDIENTS") {
                    isIngredients = true
                    isSteps = false
                    Log.i("TEXT_TRIGGER","ingredients")
                    continue
                }
                else if (block.text.uppercase() == "DIRECTIONS" || block.text.uppercase() == "INSTRUCTIONS") {
                    isIngredients = false
                    isSteps = true
                    Log.i("TEXT_TRIGGER","steps")
                    continue
                }
                if (isIngredients) {
                    Log.i("TEXT_INGREDIENT",block.text)
                    ingredients.add(block.text)
                }
                else if (isSteps && !"STEP [1-9]{1,2}".toRegex().containsMatchIn(block.text.uppercase())) {
                    Log.i("TEXT_STEP",block.text)
                    steps.add(block.text)
                }
        }
    }

}