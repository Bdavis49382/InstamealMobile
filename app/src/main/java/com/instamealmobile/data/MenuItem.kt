package com.instamealmobile.data

data class MenuItem(
    val img_link: String?,
    val title: String,
    val note: String,
    val date: String? = null,
    val recipe_id: String? = null,
    val recipe: Recipe? = null,
    val active_items: List<String>
)
