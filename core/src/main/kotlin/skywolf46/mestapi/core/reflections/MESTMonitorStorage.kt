package skywolf46.mestapi.core.reflections

import skywolf46.extrautility.util.PriorityReference
import skywolf46.mestapi.core.annotations.MESTMonitor
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object MESTMonitorStorage {


    @Suppress("DuplicatedCode")
    fun scan(mtd: Method): Boolean {
        var priority = mtd.getAnnotation(MESTMonitor::class.java)?.priority ?: return false

        if (mtd.parameterCount != 1) {
            System.err.println("Cannot parse MESTMonitor at Method ${mtd.name} in class ${mtd.declaringClass.name} : MESTProvider only accepts 1 parameter")
            return false
        }

        val method: MethodInvoker
        val kotlinCls = mtd.declaringClass.kotlin
        if (!Modifier.isStatic(mtd.modifiers)) {
            // If object class, declare like static
            method = if (kotlinCls.objectInstance != null) {
                MethodInvoker(kotlinCls.objectInstance, mtd)
            } else {
                if (kotlinCls.isCompanion) {
                    MethodInvoker(kotlinCls.objectInstance, mtd)
                } else {
                    System.err.println("Cannot parse MESTMonitor at Method ${mtd.name} in class ${mtd.declaringClass.name} : MESTMonitor requires Companion method, object class method, or JVM static method")
                    return false
                }
            }
        } else {
            // If static, always allow
            method = MethodInvoker(null, mtd)
        }
        val invoker = MethodMonitor(mtd, method)
        MESTProviderStorage.inputMap.computeIfAbsent(mtd.parameters[0].type.kotlin) {
            mutableListOf()
        }.run {
            add(PriorityReference(invoker, priority))
            sort()
            if (size <= 1) {
                System.err.println("Warning; MESTMonitor at Method ${mtd.name} in class ${mtd.declaringClass.name} has first priority, will not work")
            }
        }

        return true
    }
}