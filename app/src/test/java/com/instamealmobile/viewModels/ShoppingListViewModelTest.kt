package com.instamealmobile.viewModels

import com.instamealmobile.data.ApiState
import com.instamealmobile.data.ShoppingItem
import com.instamealmobile.data.SmallShoppingItem
import com.instamealmobile.network.ShoppingListService
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
class ShoppingListViewModelTest {
    private lateinit var testDispatcher: TestDispatcher
    private  lateinit var testScope: TestScope
    private lateinit var viewModel: ShoppingListViewModel
    val mockService = mockk<ShoppingListService>()

    @BeforeEach
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        testScope = TestScope(testDispatcher)
        viewModel = ShoppingListViewModel(mockService)
        viewModel.scope = testScope
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchShoppingList() = runTest {
        // Arrange
        var fakeList = mutableListOf<ShoppingItem>()
        fakeList.add(ShoppingItem(name="Fake Item"))
        coEvery { mockService.getShoppingList()} returns fakeList

        // Act
        viewModel.fetchShoppingList()

        // Assert
        advanceUntilIdle()
        assertTrue(viewModel.shoppingList.value is ApiState.Success)
        assertEquals(ApiState.Success(fakeList), viewModel.shoppingList.value)
    }

    @Test
    fun fetchShoppingList_fails() = runTest {
        // Arrange
        var fakeList = mutableListOf<ShoppingItem>()
        fakeList.add(ShoppingItem(name="Fake Item"))
        coEvery { mockService.getShoppingList()} throws Exception("Fake Exception")

        // Act
        viewModel.fetchShoppingList()
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.shoppingList.value is ApiState.Error)
    }

    @Test
    fun addItemToList()  = runTest {
        // Arrange
        var fakeList = mutableListOf<ShoppingItem>()
        fakeList.add(ShoppingItem(name="Fake Item"))
        coEvery { mockService.postShoppingList(any())} returns fakeList

        // Act
        viewModel.addItemToList("Fake Item")
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.shoppingList.value is ApiState.Success)
        val data = (viewModel.shoppingList.value as ApiState.Success).data
        assertEquals(1, data.size)
        assertEquals(fakeList[0].name, data[0].name)
    }

    @Test
    fun checkItem() = runTest {
        // Arrange
        var fakeList = mutableListOf<ShoppingItem>()
        fakeList.add(ShoppingItem(name="Fake Item", checked = true))
        coEvery { mockService.checkItem(any())} returns fakeList

        // Act
        viewModel.checkItem(0)
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.shoppingList.value is ApiState.Loading)
        coVerify { mockService.checkItem(any()) }
    }

    @Test
    fun editItem() = runTest {
        // Arrange
        var fakeList = mutableListOf<ShoppingItem>()
        fakeList.add(ShoppingItem(name="Fake Item"))
        coEvery { mockService.editItem(any(), any())} returns fakeList

        // Act
        viewModel.editItem(0, SmallShoppingItem(fakeList[0]))
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.shoppingList.value is ApiState.Success)
        val data = (viewModel.shoppingList.value as ApiState.Success).data
        assertEquals(1, data.size)
        assertEquals(fakeList[0].name, data[0].name)
    }

}