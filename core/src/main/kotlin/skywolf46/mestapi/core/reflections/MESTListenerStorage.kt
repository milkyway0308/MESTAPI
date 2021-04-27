package skywolf46.mestapi.core.reflections

import skywolf46.extrautility.util.PriorityReference
import skywolf46.mestapi.core.annotations.MESTListener
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

object MESTListenerStorage {
    val inputMap = mutableMapOf<KClass<*>, MutableList<PriorityReference<MethodInvoker>>>()


    fun set(mtd: Method): Boolean {
        val annot = mtd.getAnnotation(MESTListener::class.java) ?: return false

        if (mtd.parameterCount != 1) {
            System.err.println("Cannot parse MESTListener at Method ${mtd.name} in class ${mtd.javaClass.name} : MESTListener only accepts 1 parameter")
            return false
        }
        val method: MethodInvoker
        val kotlinCls = mtd.declaringClass.kotlin
        if (!Modifier.isStatic(mtd.modifiers)) {
            // If object class, declare like static
            if (kotlinCls.objectInstance != null) {
                method = MethodInvoker(kotlinCls.objectInstance, mtd)
            } else {
                if (kotlinCls.isCompanion) {
                    method = MethodInvoker(kotlinCls.objectInstance, mtd)
                } else {
                    System.err.println("Cannot parse MESTListener at Method ${mtd.name} in class ${mtd.javaClass.name} : MESTListener requires Companion method, object class method, or JVM static method")
                    return false
                }
            }
        } else {
            // If static, always allow
            method = MethodInvoker(null, mtd)
        }
        inputMap.computeIfAbsent(mtd.parameters[0].type.kotlin) {
            mutableListOf()
        }.run {
            add(PriorityReference(method, annot.priority))
            sort()
        }

        return true
    }

    fun listen(x: Any) {
        inputMap[x::class]?.forEach {
            try {
                it.data.invoke<Any>(x)
            } catch (e: Exception) {
            }
        }
    }
}