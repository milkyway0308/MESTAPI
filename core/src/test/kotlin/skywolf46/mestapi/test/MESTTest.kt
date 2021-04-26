package skywolf46.mestapi.test

import org.junit.Test
import skywolf46.mestapi.core.MESTAPI
import skywolf46.mestapi.core.annotations.MESTListener
import skywolf46.mestapi.core.annotations.MESTProvider

class MESTTest {
    companion object {
        @MESTProvider
        fun providerTest(str: String): Int {
            println("Converting $str to ${str.hashCode()}")
            return str.hashCode()
        }


        @MESTProvider
        fun providerTestDouble(int: Int): Double {
            println("Converting $int to 20.0")
            return 20.0
        }

        @MESTListener
        fun listenerTestString(str: String) {
            println("String received: $str")
        }


        @MESTListener
        fun listenerTestInt(int: Int) {
            println("Integer received: $int")
        }


        @MESTListener
        fun listenerTestDouble(double: Double) {
            println("Double received: $double")
        }
    }

    @Test
    fun test1() {
        MESTAPI.scanMEST(javaClass)
        MESTAPI.listenMEST("Test!")
    }
}