package skywolf46.mestapi.core.reflections

import java.lang.reflect.Method

class MethodInvoker(private val fromInstance: Any?, private val target: Method) {

    fun <T : Any> invoke(vararg params: Any?): T? {
        return target.invoke(fromInstance, *params) as T?
    }
}