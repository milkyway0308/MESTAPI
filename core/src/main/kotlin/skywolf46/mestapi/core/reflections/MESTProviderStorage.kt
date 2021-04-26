package skywolf46.mestapi.core.reflections

import skywolf46.mestapi.core.annotations.MESTProvider
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

object MESTProviderStorage {

    val inputMap = mutableMapOf<KClass<*>, MutableList<MethodInvoker>>()
//    val outputMap = mutableMapOf<KClass<*>, MethodInvoker>()

    fun get(cls: KClass<*>): List<MethodInvoker>? {
        return inputMap[cls]
    }


    fun set(mtd: Method): Boolean {
        if (mtd.getAnnotation(MESTProvider::class.java) == null) {
            return false
        }
        if (mtd.parameterCount != 1) {
            System.err.println("Cannot parse MESTProvider at Method ${mtd.name} in class ${mtd.javaClass.name} : MESTProvider only accepts 1 parameter")
            return false
        }

        if (mtd.returnType.equals(Void.TYPE)) {
            System.err.println("Cannot parse MESTProvider at Method ${mtd.name} in class ${mtd.javaClass.name} : MESTProvider requires return value")
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
                    System.err.println("Cannot parse MESTProvider at Method ${mtd.name} in class ${mtd.javaClass.name} : MESTProvider requires Companion method, object class method, or JVM static method")
                    return false
                }
            }
        } else {
            // If static, always allow
            method = MethodInvoker(null, mtd)
        }
        if (inputMap.containsKey(mtd.parameters[0].type.kotlin)) {
            System.err.println("Warning: MESTProvider ${mtd.name} in ${mtd.declaringClass.name} overriding provider that receive ${mtd.parameters[0].name} as parameter")
            return false
        }
        inputMap.computeIfAbsent(mtd.parameters[0].type.kotlin) { mutableListOf() }.add(method)

//        if (inputMap.containsKey(mtd.parameters[0].type.kotlin)) {
//            System.err.println("Warning: MESTProvider ${mtd.name} in ${mtd.declaringClass.name} overriding provider that returns ${mtd.parameters[0].name}")
//            return false
//        }
//        outputMap[mtd.returnType.kotlin] = method
        return true
    }

}