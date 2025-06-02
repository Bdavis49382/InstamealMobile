package com.instamealmobile.data

data class Recipe(
    val title: String,
    val img_link: String? = null,
    val src_name: String? = null,
    val time_estimate: List<String> = mutableListOf<String>(),
    val permissions_required: String = "household",
    val instructions: List<String> = mutableListOf<String>(),
    val author_id: String? = null,
    val servings: String? = null,
    val src_link: String? = null,
    val ingredients: List<String> = mutableListOf<String>(),
    val history: List<Record>? = null,
    val id: String? = null,
    val score: Float? = null,
    val rate: Float? = null,
    val index: Int = 0
)

data class Record(
    val household_id: String,
    val timestamp: String,
    val rating: Float?
)