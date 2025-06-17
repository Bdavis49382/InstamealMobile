package com.instamealmobile.viewModels

import com.instamealmobile.data.ApiState
import com.instamealmobile.data.User
import com.instamealmobile.network.HouseholdService
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
class HouseholdViewModelTest {
    private lateinit var testDispatcher: TestDispatcher
    private  lateinit var testScope: TestScope
    private lateinit var viewModel: HouseholdViewModel
    val mockService = mockk<HouseholdService>()

    @BeforeEach
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        testScope = TestScope(testDispatcher)
        viewModel = HouseholdViewModel(mockService)
        viewModel.scope = testScope
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun getUsers() = runTest {
        // Arrange
        val fakeList = mutableListOf<User>()
        coEvery { mockService.getHousehold()} returns fakeList

        // Act
        viewModel.getUsers()

        // Assert
        advanceUntilIdle()
        assertTrue(viewModel.users.value is ApiState.Success)
        val response = (viewModel.users.value as ApiState.Success).data
        assertEquals(0,response.size)
        coVerify { mockService.getHousehold()}
    }

    @Test
    fun getCode() {
    }

    @Test
    fun joinHousehold() {
    }

    @Test
    fun kickUser() {
    }

}