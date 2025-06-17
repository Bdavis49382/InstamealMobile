package com.instamealmobile.viewModels

import com.instamealmobile.data.ApiState
import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.MenuListItem
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.MenuService
import io.mockk.coEvery
import io.mockk.every
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
class MenuViewModelTest {
    private lateinit var testDispatcher: TestDispatcher
    private  lateinit var testScope: TestScope
    private lateinit var viewModel: MenuViewModel
    val mockService = mockk<MenuService>()

    @BeforeEach
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        testScope = TestScope(testDispatcher)
        viewModel = MenuViewModel(mockService)
        viewModel.scope = testScope
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getRecipe() = runTest {
        // Arrange
        val mock = mockk<MenuItem>()
        coEvery { mockService.getRecipeByIndex(any())} returns mock

        // Act
        viewModel.getRecipe(0)

        // Assert
        advanceUntilIdle()
        assertFalse(viewModel.selected.value is ApiState.Loading)
        assertTrue(viewModel.selected.value is ApiState.Success)
        val data = (viewModel.selected.value as ApiState.Success).data
        assertEquals(mock, data)
    }
    @Test
    fun addRecipe() = runTest {
        // Arrange
        val mockRecipe = mockk<Recipe>()
        every { mockRecipe.title} returns ""
        every { mockRecipe.id} returns ""
        every { mockRecipe.img_link} returns ""

        val mock = mockk<MenuListItem>()
        val fakeList = mutableListOf<MenuListItem>(mock)
        coEvery { mockService.addRecipe(any())} returns fakeList

        // Act
        viewModel.addRecipe(mockRecipe)

        // Assert
        advanceUntilIdle()
        assertFalse(viewModel.menu.value is ApiState.Loading)
        assertTrue(viewModel.menu.value is ApiState.Success)
        val data = (viewModel.menu.value as ApiState.Success).data
        assertEquals(fakeList, data)
    }

    @Test
    fun finishMeal() = runTest {
        // Arrange
        val mock = mockk<MenuListItem>()
        val fakeList = mutableListOf<MenuListItem>(mock)
        coEvery { mockService.finishMeal(any(),any())} returns fakeList

        // Act
        viewModel.finishMeal("",null)

        // Assert
        advanceUntilIdle()
        assertFalse(viewModel.menu.value is ApiState.Loading)
        assertTrue(viewModel.menu.value is ApiState.Success)
        val data = (viewModel.menu.value as ApiState.Success).data
        assertEquals(fakeList, data)
    }
}