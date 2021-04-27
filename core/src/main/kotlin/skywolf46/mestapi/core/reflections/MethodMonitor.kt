package skywolf46.mestapi.core.reflections

import java.lang.reflect.Method

class MethodMonitor(target: Method,private val real: MethodInvoker) : MethodInvoker(null, target){
    override fun <T : Any> invoke(vararg params: Any?): T? {
        real.invoke<Any>(params[0])
        return params[0] as T?
    }
}