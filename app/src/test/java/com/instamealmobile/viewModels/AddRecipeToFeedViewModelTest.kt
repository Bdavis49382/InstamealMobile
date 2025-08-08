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
import org.junit.jupiter.api.function.Executable

@OptIn(ExperimentalCoroutinesApi::class)
class AddRecipeToFeedViewModelTest {
    private lateinit var testDispatcher: TestDispatcher
    private  lateinit var testScope: TestScope
    private lateinit var viewModel: AddRecipeToFeedViewModel
    val mockService = mockk<FeedService>()

    @BeforeEach
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        testScope = TestScope(testDispatcher)
        viewModel = AddRecipeToFeedViewModel(mockService)
        viewModel.scope = testScope
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun submitRecipe() = runTest {
        // Arrange
        coEvery { mockService.addRecipe(any())} returns ""
        viewModel.ingredients.add("")
        viewModel.steps.add("")
        viewModel.title.value = "not empty"

        // Act
        val succeeded = viewModel.submitRecipe("", {})

        // Assert
        advanceUntilIdle()
        assertTrue(succeeded)
        coVerify { mockService.addRecipe(any())}
    }
    @Test
    fun setRecipe_noId() {
        // Arrange
        // Act
        viewModel.setRecipe(Recipe("",id=null))

        // Assert
        assertTrue(viewModel.ingredients.isEmpty())
        assertTrue(viewModel.steps.isEmpty())
        assertTrue(viewModel.title.value.isEmpty())
        assertTrue(viewModel.servings.value.isEmpty())
        assertTrue(viewModel.totalTime.value.isEmpty())
        assertTrue(viewModel.source.value.isEmpty())
        assertTrue(viewModel.newIngredient.isEmpty())
        assertTrue(viewModel.newStep.isEmpty())
    }

    @Test
    fun setRecipe() {
        // Arrange
        // Act
        viewModel.setRecipe(Recipe(
            title="1",
            id="1",
            instructions = mutableListOf("1"),
            ingredients = mutableListOf("1"),
            servings = "1",
            time_estimate = mutableListOf("1"),
            src_name = "1",
            img_link = "1",
        ))

        // Assert
        assertEquals("1", viewModel.ingredients[0])
        assertEquals("1",viewModel.steps[0])
        assertEquals("1",viewModel.title.value)
        assertEquals("1",viewModel.servings.value)
        assertEquals("1",viewModel.totalTime.value)
        assertEquals("1",viewModel.source.value)
        assertEquals("1", (viewModel.img_link.value as ApiState.Success).data)
    }

    @Test
    fun validateRecipe_missingAll() {
        // Arrange

        // Act
        val response = viewModel.validateRecipe()

        // Assert
        assertFalse(response)
    }

    @Test
    fun validateRecipe_missingIngredients() {
        // Arrange
        viewModel.title.value = "fake title"
        viewModel.steps.add("Fake step")

        // Act
        val response = viewModel.validateRecipe()

        // Assert
        assertFalse(response)
    }

    @Test
    fun validateRecipe_missingSteps() {
        // Arrange
        viewModel.title.value = "fake title"
        viewModel.ingredients.add("Fake ingredient")

        // Act
        val response = viewModel.validateRecipe()

        // Assert
        assertFalse(response)
    }

    @Test
    fun validateRecipe_missingTitle() {
        // Arrange
        viewModel.steps.add("Fake Step")
        viewModel.ingredients.add("Fake ingredient")

        // Act
        val response = viewModel.validateRecipe()

        // Assert
        assertFalse(response)
    }

    @Test
    fun validateRecipe() {
        // Arrange
        viewModel.steps.add("Fake Step")
        viewModel.ingredients.add("Fake ingredient")
        viewModel.title.value = "Fake Title"

        // Act
        val response = viewModel.validateRecipe()

        // Assert
        assertTrue(response)
    }

    @Test
    fun uploadImage() {
        // Perhaps test on client side.
    }

    @Test
    fun parseText() {
        // Perhaps test on client side.
    }

    @Test
    fun textToRecipe() {
        val fakeBlocks = mutableListOf<String>(
            "My Title",
            "Random Gibberish",
            "Total: 90 minutes",
            "Ingredients",
            "1 Cup Water",
            "Instructions:",
            "Fry water until browned."
        )

        viewModel.textToRecipe(fakeBlocks)

        assertEquals("My Title", viewModel.title.value)
        assertEquals("90 minutes", viewModel.totalTime.value)
        assertTrue(viewModel.ingredients.isNotEmpty())
        assertEquals("1 Cup Water", viewModel.ingredients[0])
        assertTrue(viewModel.steps.isNotEmpty())
        assertEquals("Fry water until browned.", viewModel.steps[0])
    }

    @Test
    fun textToRecipeNoIdentifiers() {
        val fakeBlocks = mutableListOf<String>(
            "My Title",
            "Random Gibberish",
            "Total: 90 minutes",
            "1 Cup Water",
            "Fry water until browned."
        )

        viewModel.textToRecipe(fakeBlocks)

        assertEquals("My Title", viewModel.title.value)
        assertEquals("90 minutes", viewModel.totalTime.value)
        assertTrue(viewModel.ingredients.isNotEmpty())
        assertEquals("1 Cup Water", viewModel.ingredients[0])
        assertTrue(viewModel.steps.isNotEmpty())
        assertEquals("Fry water until browned.", viewModel.steps[0])
    }

    @Test
    fun textToRecipeUsesContext() {
        val fakeBlocks = mutableListOf<String>(
            "My Title of Coolness",
            "Random gibberish.",
            "Total: 90 minutes",
            "1 Cup Water",
            "Special sauce", // Because this is between two ingredients it is most likely an ingredient.
            "2 tsp. salt",
            "Fry water until browned."
        )

        viewModel.textToRecipe(fakeBlocks)

        assertEquals("My Title of Coolness", viewModel.title.value)
        assertEquals("90 minutes", viewModel.totalTime.value)
        assertTrue(viewModel.ingredients.isNotEmpty())
        assertEquals("1 Cup Water", viewModel.ingredients[0])
        assertEquals("Special sauce", viewModel.ingredients[1])
        assertEquals("2 tsp. salt", viewModel.ingredients[2])
        assertTrue(viewModel.steps.isNotEmpty())
        assertEquals("Fry water until browned.", viewModel.steps[0])
    }

    @Test
    fun textToRecipe_handleWhitespace() {
        val fakeBlocks = mutableListOf<String>(
            " My Title ",
            " Random Gibberish",
            " Total:   90 minutes",
            "Ingredients:  ",
            "1 Cup Water",
            "\nInstructions:",
            "\t \nFry water until browned."
        )

        viewModel.textToRecipe(fakeBlocks)

        assertEquals("My Title", viewModel.title.value)
        assertEquals("90 minutes", viewModel.totalTime.value)
        assertTrue(viewModel.ingredients.isNotEmpty())
        assertEquals("1 Cup Water", viewModel.ingredients[0])
        assertTrue(viewModel.steps.isNotEmpty())
        assertEquals("Fry water until browned.", viewModel.steps[0])
    }

    @Test
    fun textToRecipe_handleIngredientsInstructionsSwitched() {
        val fakeBlocks = mutableListOf<String>(
            "My Title ",
            "Random Gibberish",
            "Total:   90 minutes",
            "Instructions",
            "Fry water until browned.",
            "Ingredients:  ",
            "1 Cup Water"
        )

        viewModel.textToRecipe(fakeBlocks)

        assertEquals("My Title", viewModel.title.value)
        assertEquals("90 minutes", viewModel.totalTime.value)
        assertTrue(viewModel.ingredients.isNotEmpty())
        assertEquals("1 Cup Water", viewModel.ingredients[0])
        assertTrue(viewModel.steps.isNotEmpty())
        assertEquals("Fry water until browned.", viewModel.steps[0])
    }
    @Test
    fun textToRecipe_handlesExtraWordsAroundTriggers() {
        val fakeBlocks = mutableListOf<String>(
            "My Title ",
            "Random Gibberish",
            "Total:   90 minutes",
            "Instructions for recipe",
            "Fry water until browned.",
            "ingredients:  ",
            "1 Cup Water"
        )

        viewModel.textToRecipe(fakeBlocks)

        assertEquals("My Title", viewModel.title.value)
        assertEquals("90 minutes", viewModel.totalTime.value)
        assertTrue(viewModel.ingredients.isNotEmpty())
        assertEquals("1 Cup Water", viewModel.ingredients[0])
        assertTrue(viewModel.steps.isNotEmpty())
        assertEquals("Fry water until browned.", viewModel.steps[0])
    }

    @Test
    fun textToRecipe_handlesTimeOrderSwitched() {
        val fakeBlocks = mutableListOf<String>(
            "My Title ",
            "Random Gibberish",
            "Instructions for recipe",
            "Fry water until browned.",
            " ingredients:  ",
            "1 Cup Water",
            "Total:   90 minutes"
        )

        viewModel.textToRecipe(fakeBlocks)

        assertEquals("My Title", viewModel.title.value)
        assertEquals("90 minutes", viewModel.totalTime.value)
        assertTrue(viewModel.ingredients.isNotEmpty())
        assertEquals("1 Cup Water", viewModel.ingredients[0])
        assertTrue(viewModel.steps.isNotEmpty())
        assertEquals("Fry water until browned.", viewModel.steps[0])
    }

    @Test
    fun textToIngredients() {
        val fakeBlocks = mutableListOf<String>(
            "Ingredients",
            "Ingredient 1",
            "Ingredient 2",
            "blabla",
            "blabla"
        )

        viewModel.textToIngredients(fakeBlocks)

        val expected = listOf<String>(
            "Ingredient 1",
            "Ingredient 2",
            "blabla",
            "blabla"

        )
        assertEquals(expected, viewModel.ingredients)
    }
    @Test
    fun textToIngredientsHandlesWhiteSpace() {
        val fakeBlocks = mutableListOf<String>(
            "Ingredients \n",
            " Ingredient 1  ",
            "Ingredient 2 ",
            " blabla",
            " blabla"
        )

        viewModel.textToIngredients(fakeBlocks)

        val expected = listOf<String>(
            "Ingredient 1",
            "Ingredient 2",
            "blabla",
            "blabla"

        )
        assertEquals(expected, viewModel.ingredients)
    }
    @Test
    fun textToIngredientsHandlesUpperCase() {
        val fakeBlocks = mutableListOf<String>(
            "INGREDIENTS",
            "Ingredient 1",
            "Ingredient 2",
            "blabla",
            "blabla"
        )

        viewModel.textToIngredients(fakeBlocks)

        val expected = listOf<String>(
            "Ingredient 1",
            "Ingredient 2",
            "blabla",
            "blabla"

        )
        assertEquals(expected, viewModel.ingredients)
    }
    @Test
    fun textToIngredientsHandlesColon() {
        val fakeBlocks = mutableListOf<String>(
            "ingredients:",
            "Ingredient 1",
            "Ingredient 2",
            "blabla",
            "blabla"
        )

        viewModel.textToIngredients(fakeBlocks)

        val expected = listOf<String>(
            "Ingredient 1",
            "Ingredient 2",
            "blabla",
            "blabla"

        )
        assertEquals(expected, viewModel.ingredients)
    }
    @Test
    fun textToIngredientsHandlesPartials() {
        val fakeBlocks = mutableListOf<String>(
            "ingredients: Ingredient 1",
            "Ingredient 2",
            "blabla",
            "blabla"
        )

        viewModel.textToIngredients(fakeBlocks)

        val expected = listOf<String>(
            "Ingredient 1",
            "Ingredient 2",
            "blabla",
            "blabla"

        )
        assertEquals(expected, viewModel.ingredients)
    }
    @Test
    fun textToStepsHandlesColon() {
        val fakeBlocks = mutableListOf<String>(
            "Directions:",
            "Step 1",
            "blabla",
            "Step 2",
            "blabla"
        )

        viewModel.textToSteps(fakeBlocks)

        val expected = listOf<String>(
            "blabla",
            "blabla"

        )
        assertEquals(expected, viewModel.steps)
    }
    @Test
    fun textToStepsHandlesInstructions() {
        val fakeBlocks = mutableListOf<String>(
            "Instructions",
            "Step 1",
            "blabla",
            "Step 2",
            "blabla"
        )

        viewModel.textToSteps(fakeBlocks)

        val expected = listOf<String>(
            "blabla",
            "blabla"

        )
        assertEquals(expected, viewModel.steps)
    }
    @Test
    fun textToStepsHandlesSteps() {
        val fakeBlocks = mutableListOf<String>(
            "Steps",
            "Step 1",
            "blabla",
            "Step 2",
            "blabla"
        )

        viewModel.textToSteps(fakeBlocks)

        val expected = listOf<String>(
            "blabla",
            "blabla"

        )
        assertEquals(expected, viewModel.steps)
    }
    @Test
    fun textToStepsHandlesWhiteSpace() {
        val fakeBlocks = mutableListOf<String>(
            "Instructions\n",
            "Step 1 ",
            " blabla  ",
            " Step 2 ",
            "blabla   "
        )

        viewModel.textToSteps(fakeBlocks)

        val expected = listOf<String>(
            "blabla",
            "blabla"

        )
        assertEquals(expected, viewModel.steps)
    }
    @Test
    fun textToStepsHandlesPartials() {
        val fakeBlocks = mutableListOf<String>(
            "Instructions Step 1: blabla",
            "Step 2: blabla"
        )

        viewModel.textToSteps(fakeBlocks)

        val expected = listOf<String>(
            "blabla",
            "blabla"

        )
        assertEquals(expected, viewModel.steps)
    }

    @Test
    fun evaluateBlockIngredient() {
        val expected = "ingredient"
        assertAll(
            Executable { assertEquals(expected, viewModel.evaluateBlock("1 egg")) },
            Executable { assertEquals(expected, viewModel.evaluateBlock(" 1 egg"))},
            Executable { assertEquals(expected, viewModel.evaluateBlock("a teaspoon vanilla")) },
            Executable { assertEquals(expected, viewModel.evaluateBlock("tsp. vanilla"))},
            Executable { assertEquals(expected, viewModel.evaluateBlock("IV tsp. vanilla"))},
            Executable { assertEquals("unknown", viewModel.evaluateBlock("a smattering of ketchup"))}
        )
    }

    @Test
    fun evaluateBlockStep() {
        val expected = "step"
        assertAll(
            Executable { assertEquals(expected, viewModel.evaluateBlock("Mix the ingredients together."))},
        )
    }
}