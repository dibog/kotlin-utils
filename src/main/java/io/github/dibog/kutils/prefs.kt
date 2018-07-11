package io.github.dibog.kutils

import java.io.InputStream
import java.util.*

interface ValueHolder : Iterable<String> {
    operator fun get(key: String): String?
    operator fun get(key: String, default: String): String
    operator fun get(key: String, default: ()->String): String

    fun forEach(effect: (String)->Unit) {
        iterator().forEach { key -> effect(key) }
    }

    fun forEach(effect: (String,String)->Unit) {
        iterator().forEach { key ->this[key]?.let { effect(key, it) } }
    }

    fun copy(): ValueHolder = Props().also {
        forEach { key,value -> it[key] = value }
    }
}

interface MutableValueHolder : ValueHolder {
    operator fun set(key: String, value: String)
    fun clear(key: String)
}

private fun ValueHolder.toString(obj: String): String {
    val sb = StringBuilder()
    sb.append("$obj {\n")
    forEach { key ->
        sb.append("\t$key -> ${this[key]}\n")
    }
    sb.append("}")
    return sb.toString()
}

object Env : ValueHolder {
    override operator fun get(key: String): String? = System.getenv(key)

    override operator fun get(key: String, default: String): String {
        return this[key] ?: default
    }

    override operator fun get(key: String, default: ()->String): String {
        return this[key] ?: default()
    }

    override fun iterator(): Iterator<String> {
        return System.getenv().keys.iterator()
    }

    override fun toString(): String = this.toString("Env")
}

object Sys : MutableValueHolder {
    override operator fun get(key: String): String? = System.getProperty(key)

    override operator fun get(key: String, default: String): String = this[key] ?: default

    override operator fun get(key: String, default: ()->String): String = this[key] ?: default()

    override operator fun set(key: String, value: String) {
        System.setProperty(key, value)
    }

    override fun clear(key: String) {
        System.setProperty(key, null)
    }

    override fun iterator(): Iterator<String> {
        return System.getProperties().stringPropertyNames().iterator()
    }

    override fun toString(): String = this.toString("Sys")
}

fun Properties.toProps(): MutableValueHolder {
    val props = Props()
    stringPropertyNames().forEach { key ->
        props[key] = getProperty(key)
    }
    return props
}

data class Props private constructor(val props: Properties) : MutableValueHolder {
    companion object {
        operator fun invoke(stream: InputStream) {
            Props(Properties().apply { load(stream) })
        }

        operator fun invoke(valueHolder: ValueHolder) {
            Props(Properties().apply {
                valueHolder.forEach { key ->
                    setProperty(key, valueHolder[key])
                }
            })
        }

        operator fun invoke() = Props(Properties())
    }

    override operator fun get(key: String): String? = props.getProperty(key)

    override operator fun get(key: String, default: String): String {
        return this[key] ?: default
    }

    override operator fun get(key: String, default: ()->String): String {
        return this[key] ?: default()
    }

    override operator fun set(key: String, value: String) {
        props.setProperty(key, value)
    }

    override fun clear(key: String) {
        props.remove(key)
    }

    override fun iterator(): Iterator<String> {
        return props.stringPropertyNames().iterator()
    }

    override fun toString(): String = this.toString("Props")
}

fun ValueHolder.expandMacrosInKeyAndValue(macros: Map<String,String>): Props {

    val result = Props()

    forEach { key , value ->
        result[key.expandMacros(macros)] =  value.expandMacros(macros)
    }

    return result
}

fun ValueHolder.expandMacrosInValue(macros: Map<String,String>): Props {

    val result = Props()

    forEach { key , value ->
        result[key] = value.expandMacros(macros)
    }

    return result
}
