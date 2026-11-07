package com.hardik.safehaven

import com.hardik.safehaven.core.ui.state.UiState
import com.hardik.safehaven.domain.model.ItemType
import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.domain.usecase.GetItemsUseCase
import com.hardik.safehaven.feature_home.HomeViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    // JUnit is the testing framework,
    // it helps Android developers to find test functions, execute them,
    // check whether the expected result matches the actual result
    // report pass or failure

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    // viewModel runs on Main dispatcher by default but over here we are not running
    // full Android application, we need to replace Main dispatcher with test-controlled dispatcher.

    private val getItemsUseCase: GetItemsUseCase = mockk()
    // HomeViewModel -> GetItemsUseCase -> Repository -> Database
    // we are testing HomeViewModel, Do we need to test the database, repository and use case at the same time? -> NO!
    // We want to test HomeViewModel independently.
    // So instead of using the real GetItemsUseCase, we create a fake replacement using MockK.

    // for example, we can tell it, return an empty list
    // or return three documents
    // or throw a database exception
    // we can control the response .... And this is what we need

    private lateinit var viewModel: HomeViewModel

    private fun createTestSecureItem(
        id: String = "doc_101",
        title: String = "Passport",
        content: String = "Test content",
        type: ItemType = ItemType.ID
    ): SecureItem {

        return SecureItem(
            id = id,
            title = title,
            content = content,
            type = type.name,
            createdAt = 1730000000000L
        )

    } // helper fn, it creates sample documents for our tests.

    // TARGET - HomeViewModel should emit UiState.Empty

    @Test // without this annotation, JUnit won't automatically recognise this function as a test.
    fun homeViewModel_emptyList_returnsEmptyState() =
        runTest(mainDispatcherRule.testDispatcher) { // we are telling kotlin,
            // run this coroutine based test using that test dispatcher.
            // WHY ???
            // bcz our viewmodel uses Flow and viewModelScope, and we need to test asynchronous work
            // in a controlled environment.

            // Arrange (setup the situation)
            every {
                getItemsUseCase()
            } returns flowOf(emptyList())
            // whenever someone calls getItemUseCase(), return a Flow containing an empty list.
            // this is like our fake database saying, Here we go, there are no documents

            viewModel = HomeViewModel(getItemsUseCase)
            // we create our actual HomeViewModel,
            // we pass our mocked GetItemUseCase into it,
            // the viewModel doesn't know anything about the mock
            // it simply calls getItemUseCase() and our mock responds ith flowOf(emptyList)

            val job = backgroundScope.launch( // I) launch - starts a coroutine
                UnconfinedTestDispatcher(testScheduler) // III) UnconfinedTestDispatcher - helps the collector start immediately in this test
            ) {
                viewModel.uiState.collect {  } // II) collect - starts listening to the flow
            // empty lambda means: collect the emitted values, but don't do anything with them
            }
            // Why are we collecting uiState() ?
            // Flow = is a stream of data
            // Imagine SafeHaven is watching the database, Whenever the database changes, it can send a new list of documents.
            // A flow is generally cold, that means it doesn't start doing its work until someone collects it.
            // that is why .collect { }
            // why is lambda empty ?? bcz we don't need to do anything with each individual emission.

            // Act
            advanceUntilIdle() // tells the test schedular, "execute the scheduled coroutine tasks
            // until there is no more immediate work to process."

            // Assert (Did our ViewModel behave correctly ???)
            assertTrue(
                viewModel.uiState.value.state is UiState.Empty
            ) // I expect the current state of my HomeViewModel to be UiState.Empty

            job.cancel() // We started the Flow collector, we should stop it when we no longer need it.

        }

    @Test
    fun homeViewModel_itemsAvailable_returnsSuccessState() =
        runTest(mainDispatcherRule.testDispatcher) {

            // Arrange
            val note = createTestSecureItem(
                id = "doc_101",
                title = "Personal Notes",
                type = ItemType.NOTE
            )

            val password = createTestSecureItem(
                id = "doc_102",
                title = "Gmail Password",
                type = ItemType.PASSWORD
            )

            val testItems = listOf(note, password)

            every {
                getItemsUseCase()
            } returns flowOf(testItems)

            viewModel = HomeViewModel(getItemsUseCase)

            val job = backgroundScope.launch(
                UnconfinedTestDispatcher(testScheduler)
            ) {
                viewModel.uiState.collect {  }
            }

            // Act
            advanceUntilIdle()

            // Assert
            val state = viewModel.uiState.value.state

            assertTrue(state is UiState.Success)

            val result = (state as UiState.Success).data

            assertEquals(2, result.size)
            assertEquals("doc_101", result[0].id)
            assertEquals("doc_102", result[1].id)
            assertEquals("Personal Notes", result[0].title)

            job.cancel()
        }

    @Test
    fun homeViewModel_databaseThrowsException_returnsErrorState() =
        runTest(mainDispatcherRule.testDispatcher) {

            // Arrange
            every {
                getItemsUseCase()
            } returns flow {
                throw RuntimeException("Database failure")
            }

            viewModel = HomeViewModel(getItemsUseCase)

            val job = backgroundScope.launch(
                UnconfinedTestDispatcher(testScheduler)
            ) {
                viewModel.uiState.collect {  }
            }

            // Act
            advanceUntilIdle()

            // Assert
            val state = viewModel.uiState.value.state

            assertTrue(state is UiState.Error)

            assertEquals(
                "Failed to load items",
                (state as UiState.Error).message
            )

            job.cancel()
        }

    @Test
    fun homeViewModel_initialState_returnsLoading() =
        runTest(mainDispatcherRule.testDispatcher) {

            // Arrange
            every {
                getItemsUseCase()
            } returns flow {
                kotlinx.coroutines.awaitCancellation()
            }

            // Act
            viewModel = HomeViewModel(getItemsUseCase)

            // Assert
            assertTrue(viewModel.uiState.value.state is UiState.Loading)

        }

}

// There are three important stages in testing: AAA

// 1 -> Arrange (setup the situation)

// 2 -> Act (Let the system perform its work)

// 3 -> Assert (check whether the result is correct)