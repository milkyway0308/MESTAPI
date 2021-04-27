package skywolf46.mestapi.core.data

class MESTPriority {
    companion object {
        const val RESERVED = -1000

        const val PREINIT = -1

        const val INIT = 0

        const val PROCESS = 1000

        const val HIGHEST = 100000

        const val HIGH = 110000

        const val NORMAL = 120000

        const val LOW = 130000

        const val LOWEST = 140000

        const val LAZY = Int.MAX_VALUE - 100

        const val MONITORING = Int.MAX_VALUE - 10

        const val FINALIZING = Int.MAX_VALUE - 1

        const val LISTENING = Int.MAX_VALUE
    }
}
