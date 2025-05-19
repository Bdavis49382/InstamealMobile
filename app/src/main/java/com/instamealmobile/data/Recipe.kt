package com.instamealmobile.data

data class Recipe(
    val title: String,
    val img_link: String,
    val src_name: String?,
    val time_estimate: List<String>?,
    val permissions_required: String = "household",
    val instructions: List<String>?,
    val author_id: String?,
    val servings: Float?,
    val src_link: String?,
    val ingredients: List<String>?,
    val history: List<Record>?
)

data class Record(
    val household_id: String,
    val timestamp: String,
    val rating: Int?
)