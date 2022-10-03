package com.will

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

class HelloCoroutineTest {

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `Hello`() {
        // 코루틴 빌더
        GlobalScope.launch {
            println("Coroutine start")
            delay(3000L)
            println("Coroutine end") // 처리되기 전에 메인 스레드가 먼저 종료되버림
        }
        println("Main Thread")
//        Thread.sleep(4000L)
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `Hello 1`() = runBlocking {
        // 코루틴 빌더
        val job = GlobalScope.launch {
            println("Coroutine start")
            delay(3000L)
            println("Coroutine end")
        }
        println("Main Thread")
        delay(4000L)
    }


    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `Hello 11`() = runBlocking {
        // 코루틴 빌더
        val job = GlobalScope.launch {
            println("Coroutine start")
            delay(3000L)
            println("Coroutine end")
        }
        println("Main Thread")
        job.join() // Suspends the coroutine until this job is complete.
        // 모든 자식 코루틴들의 Job들에 join 하는 것은 너무 번거러움....
    }

    @Test
    fun `Hello 2`() = runBlocking { // 코루틴 빌더로 생성된 코루틴 블록
        // 자식 코루틴이 종료될때 까지, 대기할 수 있음. (명시적으로 join() 호출 하지 않아도 된다)
        launch { // 코루틴 생성 (현재 위치한 부모 코루틴에 join을 명시적으로 호출할 필요 없이 자식 코루틴들을 실행하고 종료될 때 까지 대기 할 수 있음)
            println("Coroutine start")
            delay(3000L)
            println("Coroutine end") // 처리되기 전에 메인 스레드가 먼저 종료되버림
        }
        println("Main Thread")
    }

    /**
     * runBlocking과 달리 coroutineScope는 자식들의 종료를 기다리는 동안 현재 스레드를 블록하지 않는다.
     */
    @Test
    fun `runBlocking vs coroutineScope`() = runBlocking {
        launch {
            delay(200L)
            println("Task from runBlocking")
        }

        coroutineScope {
            launch {
                delay(500L)
                println("Task from nested launch")
            }
            delay(100L)
            println("Task from coroutine scope")
        }
        println("Coroutine scope is over")
    }

    /**
     * 중단 함수 = 특정 코루틴 컨텍스트 안에서 수행되고 있고, 코루틴 컨텍스트 안에서는 모든 중단 함수를 호출 할 수 있기 때문입니다.
     */
    private suspend fun hello() {
        delay(1000L)
        println("Suspend function")
    }

    /**
     * Global scope 에서 실행 된 코루틴은
     * 마치 데몬 스레드와 같이 자신이 속한 프로세스의 종료를 지연시키지 않고,
     * 프로세스 종료 시 함께 종료된다.
     */
    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `Global Scope like daemon thread`() = runBlocking {
        GlobalScope.launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L)
    }

}