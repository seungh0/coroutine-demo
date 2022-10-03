package com.will

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

/**
 * Coroutines always execute in some context represented by a value of the CoroutineContext type.
 *
 * Dispatchers and threads
 */
internal class CoroutineContextTest {

    @Test
    fun `Dispatchers and threads`(): Unit = runBlocking {
        launch {
            println("main runBlocking thread: ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Unconfined) {
            println("Unconfined thread: ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Default) {
            println("Default thread: ${Thread.currentThread().name}")
        }
    }

    /**
     * runBlocking and coroutineScope builders both wait for their body and all its children to complete.
     *
     * The main difference is
     * that the runBlocking method blocks the current thread for waiting,
     * while coroutineScope just suspends, releasing the underlying thread for other usage
     */
    @Test
    fun `Scope Builder`() = runBlocking {
        clock()
    }

    @Test
    fun `withContext Test`() = runBlocking {
        withContext(context = Dispatchers.IO) {
            launch {
                delay(timeMillis = 1000L)
                println("어렵자나")
            }
            println("Dispatcher.. Context....")
        }
    }

    private suspend fun clock() = coroutineScope {
        launch {
            delay(timeMillis = 1000L)
            println("World")
        }
        println("Hello")
    }

}