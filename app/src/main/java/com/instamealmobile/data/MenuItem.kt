package com.instamealmobile.data

import java.util.Date


data class MenuItem(
    val img_link: String?,
    val title: String,
    val note: String,
    val date: Date? = null,
    val recipe_id: String? = null,
    val recipe: Recipe? = null,
    val active_items: List<String>
)
