package com.instamealmobile.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.instamealmobile.BuildConfig
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.FeedService
import com.instamealmobile.network.MenuService
import com.instamealmobile.ui.ImagePurpose
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.io.InvalidObjectException
import javax.inject.Inject

enum class Purpose {
    MenuEdit, FeedEdit, AddNew
}
@HiltViewModel
class AddRecipeToFeedViewModel @Inject constructor(private val apiService: FeedService, private val menuApiService: MenuService): ViewModel() {
    var ingredients = mutableStateListOf<String>()
    var tags = mutableStateListOf<String>()
    var title =  mutableStateOf("")
    var servings = mutableStateOf("")
    var totalTime = mutableStateOf("")
    var source = mutableStateOf("")
    var newIngredient by mutableStateOf("")
    var newStep by mutableStateOf("")
    var newTag by mutableStateOf("")
    var authorId by mutableStateOf("")
    private val _img_link = MutableStateFlow<ApiState<String>>(ApiState.Resting)
    val img_link: MutableStateFlow<ApiState<String>> = _img_link
    var steps = mutableStateListOf<String>()
    var validatorsActive = mutableStateOf(false)
    var fullRecipe = mutableStateOf(Recipe(title=""))
    var src_link = mutableStateOf("")
    // When updating a menu recipe, keep track of its index.
    var menuIndex by mutableStateOf(0)
    var scope = viewModelScope
    val dontCapitalize = setOf<String>("a","and","as","at","but","by","down","for","from","if","in","into","like","near","nor","of","off","on","once","onto","or","over","past","so","than","that","to","upon","when","with","yet")

    fun setRecipe(recipe: Recipe) {
        if (recipe.id.isNullOrEmpty()) {
            ingredients.clear()
            title.value = ""
            servings.value = ""
            totalTime.value = ""
            src_link.value = ""
            tags.clear()
            source.value = ""
            authorId = ""
            _img_link.value = ApiState.Resting
            steps.clear()
            fullRecipe.value = Recipe(title="")

        } else {
            ingredients.clear()
            ingredients.addAll(recipe.ingredients)
            tags.clear()
            tags.addAll(recipe.tags)
            title.value = recipe.title
            src_link.value = recipe.src_link ?: ""
            servings.value = recipe.servings?: ""
            totalTime.value = if (recipe.time_estimate.size > 0) recipe.time_estimate[0] else ""
            source.value = recipe.src_name?: ""
            _img_link.value = ApiState.Success(recipe.img_link?:"")
            steps.clear()
            steps.addAll(recipe.instructions)
            authorId = recipe.author_id?: ""
            menuIndex = recipe.index
            fullRecipe.value = recipe
        }
    }
    fun validateRecipe() : Boolean {
        return ingredients.isNotEmpty() &&
                title.value.isNotEmpty() &&
                steps.isNotEmpty()
    }

    fun submitRecipe(id: String?, confirm: (Recipe) -> Unit): Boolean {
        if (validateRecipe()) {
            scope.launch {
                try {
                    val response = if (id.isNullOrEmpty()) {
                        apiService.addRecipe(
                            Recipe(
                                ingredients = ingredients,
                                title = title.value,
                                servings = servings.value,
                                tags = tags.map {cleanTag(it)}.toMutableList(),
                                time_estimate = if (totalTime.value.isNotEmpty()) listOf(totalTime.value) else listOf(),
                                src_name = source.value,
                                img_link = if (img_link.value is ApiState.Success) {
                                    (img_link.value as? ApiState.Success)?.data
                                } else "",
                                instructions = steps
                            )
                        )
                    } else {
                        apiService.updateRecipe(
                            id, Recipe(
                                ingredients = ingredients,
                                tags = tags.map {cleanTag(it)}.toMutableList(),
                                title = title.value,
                                servings = servings.value,
                                src_link = src_link.value,
                                time_estimate = if (totalTime.value.isNotEmpty()) listOf(totalTime.value) else listOf(),
                                src_name = source.value,
                                img_link = if (img_link.value is ApiState.Success) {
                                    (img_link.value as? ApiState.Success)?.data
                                } else "",
                                instructions = steps,
                                author_id = authorId
                            )
                        )
                    }
                    confirm(Recipe(title = "", id = response, index = menuIndex))
                } catch (e: Exception) {
                    Log.e("RECIPE_SUBMISSION", e.message ?: "Issue with submitting a new recipe")
                }
            }
            return true
        } else {
            return false
        }
    }

    fun addTag(name: String) {
        tags.add(cleanTag(name))
    }

    fun cleanTag(name: String) : String {
        // Remove hashtags and spaces and Pascal case it.
        return name.replace("#","").split(" ").joinToString(separator = "") { it.replaceFirstChar { it.uppercase() } }
    }

    fun uploadImage(uri: Uri, context: Context, after: () -> Unit) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)

            val requestBody = inputStream?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
            val fileName = if (!uri.path.isNullOrEmpty()) {
                File(uri.path).name
            } else "Default_Image_Name"
            val multipartRequest = MultipartBody.Part.createFormData("file", fileName, requestBody!!)
            scope.launch {
                try {
                    _img_link.value = ApiState.Loading
                    val response = apiService.uploadImage(multipartRequest)
                    val imageUrl = "${BuildConfig.BACKEND_URL}/feed/image/$response "
                    _img_link.value = ApiState.Success(imageUrl)
                    after()
                } catch (e: Exception) {
                    _img_link.value = ApiState.Error("Failed to fetch data: ${e.message}")
                }
            }

        } catch (e: Exception) {
            Log.e("FILE_UPLOAD","file wasn't able to be processed or uploaded: ${e.message} ${e.stackTrace}")
        }
    }

    fun parsePdf(uri: Uri, context: Context, onceFinished: (List<String>) -> Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)!!
                val reader = PdfReader(inputStream)
                val pages = reader.numberOfPages
                val lines = mutableListOf<String>()
                for (i in 1..pages) {
                    val page = PdfTextExtractor.getTextFromPage(reader, i,
                        LocationTextExtractionStrategy()).trim()
                    lines.addAll(page.split("\n").filter { it.isNotBlank()})
                }
                reader.close()
                textToRecipe(lines)
                onceFinished(lines)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun parseText(uri: Uri, context: Context, purpose: ImagePurpose, onceFinished: (String) -> Unit ) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image: InputImage
        try {
            image = InputImage.fromFilePath(context, uri)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val stringBlocks = visionText.textBlocks.map {it.text}
                    when (purpose) {
                        ImagePurpose.TextParsing -> textToRecipe(stringBlocks)
                        ImagePurpose.TextParsingIngredients -> textToIngredients(stringBlocks)
                        ImagePurpose.TextParsingSteps -> textToSteps(stringBlocks)
                        else -> throw InvalidObjectException("Image meant for text parsing was incorrectly labeled as being for storage.")
                    }
                    onceFinished(visionText.text)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun parseWebsite(after: () -> Unit, onBadLink: () -> Unit, onBadWebsite: () -> Unit) {
        try {
            scope.launch {
                try {
                    val recipe = menuApiService.getRecipeOnline(src_link.value)
                    title.value = recipe.title
                    source.value = recipe.src_name ?: ""
                    servings.value = recipe.servings ?: ""
                    totalTime.value = recipe.time_estimate.firstOrNull() ?: ""
                    _img_link.value = ApiState.Success(recipe.img_link?:"")
                    ingredients.clear()
                    ingredients.addAll(recipe.ingredients)
                    steps.clear()
                    steps.addAll(recipe.instructions)
                    after()
                } catch (e: HttpException) {
                    if (e.code() == 404) {
                        onBadLink()
                    } else if (e.code() == 400 || e.code() == 500) {
                        onBadWebsite()
                    }
                }
            }

        } catch (e: Exception) {
            Log.e("FILE_UPLOAD","file wasn't able to be processed or uploaded: ${e.message} ${e.stackTrace}")
        }

    }

    fun textToIngredients(stringList: List<String>) {
        for (string in stringList) {
            if (string.isNotBlank()) {
                ingredients.addAll(
                    string.replace("Ingredients:?".toRegex(RegexOption.IGNORE_CASE),"").trim().split("\n").filter { it.isNotBlank() }
                )
            }
        }
    }
    fun textToSteps(stringList: List<String>) {
        steps.addAll(stringList.map {
            it
                .replace("\\b(?:Directions|Instructions|steps):?".toRegex(RegexOption.IGNORE_CASE),"").trim()
                .replace("Step [0-9]{1,2}:?".toRegex(RegexOption.IGNORE_CASE),"").trim()
        }.filter {
            it.isNotBlank()
        })
    }

    fun textToRecipe(stringList: List<String>) {
        ingredients.clear()
        steps.clear()
        var titleFound = false
        val guesses = stringList.map(::evaluateBlock)
        guesses.zip(stringList).forEachIndexed { index, value ->
            when (value.first) {
                "title" -> {
                    if (!titleFound) {
                        title.value = value.second.trim()
                        titleFound = true
                    }
                }
                "servings" -> servings.value = value.second.replace("Servings:","", ignoreCase = true).trim()
                "totalTime" -> totalTime.value = value.second.replace("Total:","", ignoreCase = true).trim()
                "ingredient" -> ingredients.add(value.second.trim())
                "step" -> {
                    if (index != 0 && guesses[index - 1] != "step") {
                        steps.clear()
                    }
                    steps.add(value.second.replace(Regex("^\\d{1,2}\\.?"),"").trim())
                }
                "unknown" -> {
                    if(index > 0 && index < guesses.size - 1 && guesses[index - 1] == guesses[index + 1]) {
                        if (guesses[index-1] == "ingredient") {
                            ingredients.add(value.second.trim())
                        } else if (guesses[index-1] == "step") {
                            steps.add(value.second.trim())
                        }
                    } else if (index == 0) {
                        title.value = value.second.trim()
                    } else {
                        ingredients.add(value.second.trim())
                    }
                }
            }
        }

    }
    fun wordBelongsInTitle(word: String) : Boolean {
        return if(word.isEmpty() || dontCapitalize.contains(word))
            true
        else
            word.first().isUpperCase()
    }
    fun evaluateBlock(block: String) : String {
        val results = mutableMapOf<String,Int>()
        if (block.trim().split(" ").all(::wordBelongsInTitle)) {
            results.put("title",1)
        }
        if (block.trim().first().isDigit()) {
            if (block.trim().length > 1 && block.trim()[1] == '.') {
                results.put("step", 1)
            } else {
                results.put("ingredient",1)
            }
        }
        val pattern = Regex("CUPS?|TEASPOONS?|TABLESPOONS?|DASH|PINCH|LITERS?|GALLONS?|POUNDS?|OUNCES?|OZ\\.?|TSP\\.?|T",
            RegexOption.IGNORE_CASE)
        val match = block.split(" ").any {pattern.matches(it) }
        if (match) {
            results.put("ingredient", results.getOrDefault("ingredient",0) + 1)
        }
        if (block.trim().last() == '.') {
            results.put("step",results.getOrDefault("step",0) + 2)
        }
        if (block.trim().contains(Regex("COPYRIGHT|INGREDIENTS|STEPS|DIRECTIONS|INSTRUCTIONS", RegexOption.IGNORE_CASE))) {
            results.put("junk", 1)
        }
        if (block.trim().contains("TOTAL:", ignoreCase = true)) {
            results.put("totalTime", 1)
        }
        if (block.trim().contains("SERVINGS:", ignoreCase = true)) {
            results.put("servings", 1)
        }
        val servingMatch = Regex("Makes ([0-9]{1,2}-?[0-9]{0,2}) Servings", RegexOption.IGNORE_CASE).find(block.trim())
        if (servingMatch != null && !servingMatch.groupValues.isEmpty()) {
            results.put("junk", 1)
            servings.value = servingMatch.groupValues.first()
        }
        if (results.isEmpty()) {
            return "unknown"
        }
        return results.maxBy { it.value }.key
    }

}