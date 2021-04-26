package skywolf46.mestapi.core

import skywolf46.mestapi.core.reflections.MESTListenerStorage
import skywolf46.mestapi.core.reflections.MESTProviderStorage
import kotlin.reflect.full.companionObject

object MESTAPI {
    fun scanMEST(cls: Class<*>) {
        for (x in cls.declaredMethods) {
            MESTProviderStorage.set(x)
            MESTListenerStorage.set(x)
        }
        if (!cls.kotlin.isCompanion) {
            cls.kotlin.companionObject?.java?.run {
                for (x in declaredMethods) {
                    MESTProviderStorage.set(x)
                    MESTListenerStorage.set(x)
                }
            }
        }
    }

    fun listenMEST(any: Any) {
        var next: Any? = any
        val proceed = mutableListOf<Any>()
        while (next != null) {
            val process = MESTProviderStorage.get(next::class)
            if (process != null) {
                var changed = false
                MESTListenerStorage.listen(next)
                for (x in process) {
                    val nextVal: Any? = x.invoke(next)
                    if (nextVal != null) {
                        if (proceed.contains(x)) {
                            throw IllegalStateException("Reculsive MEST processing ")
                        }
                        next = nextVal
                        proceed.add(x)
                        changed = true
                        break
                    }
                }
                if (!changed)
                    break
            } else {
                MESTListenerStorage.listen(next)
                break
            }
        }
    }
}