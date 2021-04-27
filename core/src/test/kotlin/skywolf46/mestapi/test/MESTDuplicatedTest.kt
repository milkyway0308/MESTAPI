package skywolf46.mestapi.test

import org.junit.Test
import skywolf46.mestapi.core.MESTAPI
import skywolf46.mestapi.core.annotations.MESTConstructor
import skywolf46.mestapi.core.annotations.MESTMonitor
import skywolf46.mestapi.core.data.MESTPriority

class MESTDuplicatedTest {
    companion object {
        @MESTConstructor(priority = MESTPriority.INIT)
        fun baseReplacerStringPreInit(str: String): Int {
//            assertEquals("test!", str)
            return 0
        }

        @MESTConstructor(priority = MESTPriority.PROCESS)
        fun baseReplacerStringInit(str: String): Int {
//            assertEquals("XR0", str)
            return 2
        }

        @MESTConstructor(priority = MESTPriority.LAZY)
        fun baseReplacerStringLazy(str: String): Int {
//            assertEquals("PX7", str)
            return 99
        }


        @MESTConstructor(priority = MESTPriority.PREINIT)
        fun baseReplacerIntegerPreInit(int: Int): Int {
//            assertEquals(0, int)
            return 1
        }


        @MESTConstructor(priority = MESTPriority.INIT)
        fun baseReplacerIntegerInit(int: Int): String {
//            assertEquals(1, int)
            return "XR0"
        }


        @MESTConstructor(priority = MESTPriority.HIGHEST)
        fun baseReplacerIntegerHighest(int: Int): String {
//            assertEquals(2, int)
            return "PX7"
        }


        @MESTMonitor(priority = MESTPriority.LISTENING)
        fun baseListenerIntegerListening(int: Int) {
//            assertEquals(99, int)
        }

        @MESTMonitor(priority = MESTPriority.RESERVED)
        fun baseMonitorTestReserved(str: String) {
//            assertEquals("test!", str)
        }


        @MESTMonitor(priority = MESTPriority.PROCESS - 1)
        fun baseMonitorTestInit(str: String) {
//            assertEquals("XR0", str)
        }
    }

    @Test
    fun onTest() {
        MESTAPI.scanMEST(javaClass)
        MESTAPI.listenMEST("test!")
    }


    @Test
    fun onTestPerformance() {
        val testEpoch = 1000000
        println("Warming up...")
        for (x in 0..40000) {
            var xi = x * 400
            xi += 49
            xi /= 6
        }
        println("Initializing MEST...")
        var time = System.currentTimeMillis()
        var timeNano = System.nanoTime()
        for (x in 0..5)
            MESTAPI.scanMEST(javaClass)
        println("Elapsed ${System.currentTimeMillis() - time}ms(${(System.nanoTime() - timeNano) / 1000}µs) on initiailizing")
        time = System.currentTimeMillis()
        timeNano = System.nanoTime()
        for (x in 1..testEpoch) {
            MESTAPI.listenMEST("test!")
        }
        val elapsedNano = System.nanoTime() - timeNano
        println("Elapsed ${System.currentTimeMillis() - time}ms(${elapsedNano / 1000}µs) on test in $testEpoch times / Consuming ${elapsedNano / testEpoch}ns per task")
    }
}