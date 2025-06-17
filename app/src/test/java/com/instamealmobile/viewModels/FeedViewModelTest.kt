package com.instamealmobile.viewModels

import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.FeedService
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
class FeedViewModelTest {
    private lateinit var testDispatcher: TestDispatcher
    private  lateinit var testScope: TestScope
    private lateinit var viewModel: FeedViewModel
    val mockService = mockk<FeedService>()

    @BeforeEach
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        testScope = TestScope(testDispatcher)
        viewModel = FeedViewModel(mockService)
        viewModel.scope = testScope
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun fetchFeed() = runTest {
        // Arrange
        val fakeList = mutableListOf<Recipe>()
        coEvery { mockService.getFeed()} returns fakeList

        // Act
        viewModel.fetchFeed()

        // Assert
        advanceUntilIdle()
        assertTrue(viewModel.feed.value is ApiState.Success)
        val response = (viewModel.feed.value as ApiState.Success).data
        assertEquals(0,response.size)
        coVerify { mockService.getFeed()}
    }

    @Test
    fun refreshFeed() = runTest {
        // Arrange
        val fakeList = mutableListOf<Recipe>()
        coEvery { mockService.getFeed()} returns fakeList

        // Act
        viewModel.refreshFeed()
        assertTrue(viewModel.isRefreshing)

        // Assert
        advanceUntilIdle()
        assertFalse(viewModel.isRefreshing)
        assertTrue(viewModel.feed.value is ApiState.Success)
        val response = (viewModel.feed.value as ApiState.Success).data
        assertEquals(0,response.size)
        coVerify { mockService.getFeed()}
    }

    @Test
    fun searchFeed() = runTest {
        // Arrange
        val fakeList = mutableListOf<Recipe>()
        coEvery { mockService.searchFeed(any())} returns fakeList

        // Act
        viewModel.searchFeed("")

        // Assert
        advanceUntilIdle()
        assertTrue(viewModel.feed.value is ApiState.Success)
        val response = (viewModel.feed.value as ApiState.Success).data
        assertEquals(0,response.size)
        coVerify { mockService.searchFeed(any())}
    }

}