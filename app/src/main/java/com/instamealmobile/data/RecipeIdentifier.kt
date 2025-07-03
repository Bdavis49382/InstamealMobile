package com.instamealmobile.data

sealed class RecipeIdentifier() {
    data class RecipeId(val id: String) : RecipeIdentifier()
    data class RecipeLink(val link: String, val tags: List<String>) : RecipeIdentifier()
    data class FullRecipe(val recipe: Recipe): RecipeIdentifier()
    data class RecipeContent(val recipe: Recipe): RecipeIdentifier()
    data class MenuIndex(val index: Int): RecipeIdentifier()
    companion object {
        fun factory(recipe: Recipe?): RecipeIdentifier? {
            return if(recipe == null) {
                null
            } else if (recipe.id != null && recipe.title.isNotEmpty() && !recipe.ingredients.isNullOrEmpty()) {
                FullRecipe(recipe)
            } else if (recipe.id != null) {
                RecipeId(recipe.id)
            } else if (recipe.title.isNotEmpty() && !recipe.ingredients.isNullOrEmpty()){
                RecipeContent(recipe)
            } else if (recipe.src_link != null){
                RecipeLink(recipe.src_link, tags=recipe.tags)
            } else {
                null
            }
        }

    }
}