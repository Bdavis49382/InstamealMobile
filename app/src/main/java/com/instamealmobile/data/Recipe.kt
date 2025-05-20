package com.instamealmobile.data

data class Recipe(
    val title: String,
    val img_link: String,
    val src_name: String? = null,
    val time_estimate: List<String>? = null,
    val permissions_required: String = "household",
    val instructions: List<String>? = null,
    val author_id: String? = null,
    val servings: Float? = null,
    val src_link: String? = null,
    val ingredients: List<String>? = null,
    val history: List<Record>? = null,
    val id: String? = null,
    val score: Int? = null,
    val rate: Float? = null
)

data class Record(
    val household_id: String,
    val timestamp: String,
    val rating: Int?
)