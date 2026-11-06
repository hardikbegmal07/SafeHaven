package com.hardik.safehaven

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    } // runs before each test,
    // it says "whenever the code uses Dispatcher.Main, use the test dispatcher instead.

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    } // runs after each test, it stores original MainDispatcher
} // we need this MainDispatcherRule because we don't want one test's dispatcher
// configuration affecting another test.

// TestDispatcher - A dispatcher designed for testing coroutines,
// it lets us control coroutine execution.

// StandardTestDispatcher() - This is a specific test dispatcher, unlike an ordinary dispatcher that
// starts executing work immediately,
// it generally queues coroutine work until the test schedular is told to run it, this gives us control.

// TestWatcher() - JUnit provides this class to run some code automatically before and after a test.