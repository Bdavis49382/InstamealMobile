package com.instamealmobile.viewModels

import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.data.RecipeIdentifier.RecipeId
import com.instamealmobile.data.RecipeIdentifier.RecipeLink
import com.instamealmobile.network.MenuService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeViewModelTest {
    private lateinit var testDispatcher: TestDispatcher
    private  lateinit var testScope: TestScope
    private lateinit var viewModel: RecipeViewModel
    val mockService = mockk<MenuService>()

    @BeforeEach
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        testScope = TestScope(testDispatcher)
        viewModel = RecipeViewModel(mockService)
        viewModel.scope = testScope
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun getRecipe_withId() = runTest {
        // Arrange
        coEvery { mockService.getRecipe(any())} returns Recipe(title="")

        // Act
        viewModel.getRecipe(RecipeId(""))

        // Assert
        advanceUntilIdle()
        assertTrue(viewModel.recipe.value is ApiState.Success)
        val response = (viewModel.recipe.value as ApiState.Success).data
        assertEquals("",response.title)
        coVerify { mockService.getRecipe(any())}
    }

    @Test
    fun getRecipe_withLink() = runTest {
        // Arrange
        coEvery { mockService.getRecipeOnline(any())} returns Recipe(title="")

        // Act
        viewModel.getRecipe(RecipeLink("",listOf("mainDishes")))

        // Assert
        advanceUntilIdle()
        println(viewModel.recipe.value)
        assertTrue(viewModel.recipe.value is ApiState.Success)
        val response = (viewModel.recipe.value as ApiState.Success).data
        assertEquals("",response.title)
        coVerify { mockService.getRecipeOnline(any())}
    }

}