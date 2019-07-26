package io.github.dibog.kutils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread
import kotlin.reflect.KProperty



class ThreadlocalDelegate<E>(initializer: ()->E) {
    private val threadLocal = ThreadLocal.withInitial(initializer)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): E {
        return threadLocal.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: E) {
        threadLocal.set(value)
    }
}

fun <E> threadlocal(initializer: ()->E) = ThreadlocalDelegate(initializer)

fun main() {

    val foo by lazy { 10 }

    val sdf by threadlocal { SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }}

    for(i in 0 until 10 ) {
        thread {
            println("${Thread.currentThread().name} - ${System.identityHashCode(sdf)}) - ${sdf.format(Date())}")
        }
    }

}