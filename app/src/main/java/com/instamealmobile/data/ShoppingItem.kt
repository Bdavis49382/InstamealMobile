package com.instamealmobile.data

data class ShoppingItem(
    val user_id: String? = null,
    var checked: Boolean = false,
    val recipe_id: String? = null,
    val name: String,
    val user_initial: String = "",
    val recipe_title: String = "",
    var index: Int = 0
    )

data class SmallShoppingItem(
    val user_id: String,
    val checked: Boolean = false,
    val recipe_id: String?,
    var name: String
) {
    constructor(item: ShoppingItem) : this(
        user_id = item.user_id?: "",
        checked = item.checked,
        recipe_id = item.recipe_id,
        name = item.name
    )
}
