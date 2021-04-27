package skywolf46.mestapi.core.reflections

import skywolf46.extrautility.util.PriorityReference
import skywolf46.mestapi.core.annotations.MESTConstructor
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

object MESTProviderStorage {

    internal val inputMap = mutableMapOf<KClass<*>, MutableList<PriorityReference<MethodInvoker>>>()
//    val outputMap = mutableMapOf<KClass<*>, MethodInvoker>()



    fun get(cls: KClass<*>): List<PriorityReference<MethodInvoker>>? {
        return inputMap[cls]
    }


    fun set(mtd: Method): Boolean {
        var priority = mtd.getAnnotation(MESTConstructor::class.java)?.priority ?: return false

        if (mtd.parameterCount != 1) {
            System.err.println("Cannot parse MESTProvider at Method ${mtd.name} in class ${mtd.declaringClass.name} : MESTProvider only accepts 1 parameter")
            return false
        }

        if (mtd.returnType.equals(Void.TYPE)) {
            System.err.println("Cannot parse MESTProvider at Method ${mtd.name} in class ${mtd.declaringClass.name} : MESTProvider requires return value")
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
                    System.err.println("Cannot parse MESTProvider at Method ${mtd.name} in class ${mtd.declaringClass.name} : MESTProvider requires Companion method, object class method, or JVM static method")
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
            add(PriorityReference(method, priority))
            sort()
//            println("Sorting: ${map{x -> x.priority}}")
        }
        return true
    }

}