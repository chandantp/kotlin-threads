package com.starter

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

data class Counter(@Volatile var x: Int = 0) {
    val lock = ReentrantLock()
    val condition = lock.newCondition()
    var lastThread = ""

    @Synchronized fun increment() {
        x++
    }

    fun incrementUsingLock() {
        lock.withLock {
            x++
        }
    }

    fun incrementUsingCondition() {
        lock.withLock {
            val threadName = Thread.currentThread().name
            if (threadName == lastThread) {
                condition.await()
            }
            x++
            println("$threadName: $x")
            lastThread = threadName
            condition.signal()
        }
    }
}

fun main() {

    val sharedCounter = Counter()
    println("Initial: Counter = ${sharedCounter.x}")

    val t1 = createCounterThreadWithoutSync(sharedCounter, "T1", 1_000_000)
    val t2 = createCounterThreadWithoutSync(sharedCounter, "T2", 1_000_000)

    t1.start()
    t2.start()

    t1.join()
    t2.join()

    println("After multi-thread WOUT sync: Counter = ${sharedCounter.x}")

    sharedCounter.x = 0
    val t3 = createCounterThreadWithSyncBlock(sharedCounter, "T3", 1_000_000)
    val t4 = createCounterThreadWithSyncBlock(sharedCounter, "T4", 1_000_000)

    t3.start()
    t4.start()

    t3.join()
    t4.join()

    println("After multi-thread WITH sync block: Counter = ${sharedCounter.x}")

    sharedCounter.x = 0
    val t5 = createCounterThreadWithSyncFunc(sharedCounter, "T5", 1_000_000)
    val t6 = createCounterThreadWithSyncFunc(sharedCounter, "T6", 1_000_000)

    t5.start()
    t6.start()

    t5.join()
    t6.join()

    println("After multi-thread WITH sync func: Counter = ${sharedCounter.x}")

    sharedCounter.x = 0
    val t7 = createCounterThreadWithSyncLock(sharedCounter, "T7", 1_000_000)
    val t8 = createCounterThreadWithSyncLock(sharedCounter, "T8", 1_000_000)

    t7.start()
    t8.start()

    t7.join()
    t8.join()

    println("After multi-thread WITH sync using locks: Counter = ${sharedCounter.x}")

    sharedCounter.x = 0
    println()
    println("Start counting...")
    val t9 = createCounterThreadAlternating(sharedCounter, "T1", 20)
    val t10 = createCounterThreadAlternating(sharedCounter, "T2", 20)

    t9.start()
    t10.start()

    t9.join()
    t10.join()

}

private fun createCounterThreadWithoutSync(counter: Counter, threadName: String, totalSteps: Int): Thread {
    return thread(start = false, name = threadName) {
        for(i in 1..totalSteps) {
            counter.x++
        }
    }
}

private fun createCounterThreadWithSyncBlock(counter: Counter, threadName: String, totalSteps: Int): Thread {
    return thread(start = false, name = threadName) {
        for(i in 1..totalSteps) synchronized(counter) {
            counter.x++
        }
    }
}

private fun createCounterThreadWithSyncFunc(counter: Counter, threadName: String, totalSteps: Int): Thread {
    return thread(start = false, name = threadName) {
        for(i in 1..totalSteps) counter.increment()
    }
}

private fun createCounterThreadWithSyncLock(counter: Counter, threadName: String, totalSteps: Int): Thread {
    return thread(start = false, name = threadName) {
        for(i in 1..totalSteps) counter.incrementUsingLock()
    }
}

private fun createCounterThreadAlternating(counter: Counter, threadName: String, totalSteps: Int): Thread {
    return thread(start = false, name = threadName) {
        for(i in 1..totalSteps) {
            counter.incrementUsingCondition()
        }
    }
}