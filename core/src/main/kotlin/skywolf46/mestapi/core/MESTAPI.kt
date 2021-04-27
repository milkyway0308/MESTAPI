package skywolf46.mestapi.core

import skywolf46.extrautility.util.PriorityReference
import skywolf46.mestapi.core.reflections.*
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
                    MESTMonitorStorage.scan(x)
                    MESTListenerStorage.set(x)
                }
            }
        }
    }

    fun listenMEST(any: Any) {
        var next: Any? = any
//        val proceed = mutableListOf<Any>()
        val unused = mutableMapOf<Class<*>, MutableList<PriorityReference<MethodInvoker>>>()
        while (next != null) {
            val process =
                if (unused.containsKey(next::class.java)) unused[next::class.java]!! else MESTProviderStorage.get(next::class)
                    ?.let {
                        val ml = it.toMutableList()
                        unused[next!!::class.java] = ml
                        return@let ml
                    }
            if (process != null) {
                with(process.iterator()) {
                    while (hasNext()) {
                        val nxt = next()
                        // Listen monitor first
                        if (nxt.data is MethodMonitor) {
                            nxt.data.invoke<Any>(next)
                            remove()
                        } else {
                            break
                        }
                    }
                }
                var changed = false
                MESTListenerStorage.listen(next)
                if (process.isNotEmpty()) {
                    for (x in ArrayList(process).also {
                        with(it.iterator()) {
                            while (hasNext())
                                if (next().data is MethodMonitor)
                                    remove()
                        }
                    }) {
                        if (x.data is MethodMonitor)
                            continue
                        val nextVal: Any? = x.data.invoke(next)
                        if (nextVal != null) {
                            process.remove(x)
                            next = nextVal
                            changed = true
                            break
                        }
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