package com.will

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

internal class SequentialByDefaultTest {

    @Test
    fun `Sequential by default`() = runBlocking {
        val time = measureTimeMillis {
            val one = doSomethingOne()
            val two = doSomethingTwo()
            val result = one + two
            println("Result: $result")
        }

        println("Completed in $time ms")
    }

    @Test
    fun `Concurrent using async`() = runBlocking {
        val time = measureTimeMillis {
            val one: Deferred<Int> = async { doSomethingOne() }
            val two: Deferred<Int> = async { doSomethingTwo() }
            val result = one.await() + two.await()
            println("Result: $result")
        }

        println("Completed in $time ms")
    }

    @Test
    fun `Lazily started async`() = runBlocking {
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) { doSomethingOne() }
            val two = async(start = CoroutineStart.LAZY) { doSomethingTwo() }

            one.start()
            two.start()

            val result = one.await() + two.await()
            println("Result: $result")
        }

        println("Completed in $time ms")
    }

    private suspend fun doSomethingOne(): Int {
        delay(timeMillis = 1000L)
        return 13
    }

    private suspend fun doSomethingTwo(): Int {
        delay(timeMillis = 2000L)
        return 5
    }

    @Test
    fun `Async-Style functions`() = runBlocking {
        val time = measureTimeMillis {
            val one = doSomethingOneAsync()
            val two = doSomethingTwoAsync()

            println("result: ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun doSomethingOneAsync() = GlobalScope.async {
        doSomethingOne()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun doSomethingTwoAsync() = GlobalScope.async {
        doSomethingTwo()
    }

}