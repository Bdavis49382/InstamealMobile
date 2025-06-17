package com.instamealmobile.data

sealed class RecipeIdentifier() {
    data class RecipeId(val id: String) : RecipeIdentifier()
    data class RecipeLink(val link: String) : RecipeIdentifier()
//    data class RecipeContent(val content: Recipe) : RecipeIdentifier()
    companion object {
        fun factory(recipe: Recipe): RecipeIdentifier {
            return if (recipe.id != null) {
                RecipeId(recipe.id)
            } else {
                RecipeLink(recipe.src_link?: "") // TODO: Fix later, possibly make src_link non-nullable, or just throw error
            }
//            } else {
//                RecipeContent(recipe)
//            }
        }

    }
}