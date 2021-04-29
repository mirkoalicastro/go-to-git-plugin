package com.mirkoalicastro.gotogit.safe

import kotlin.reflect.KProperty

fun <T> tryLazy(initializer: () -> T?) = TryLazy(initializer)

class TryLazy<T>(initializer: () -> T?) {
    private val value: T? by lazy {
        try {
            initializer()
        } catch (e: Exception) {
            null
        }
    }

    operator fun getValue(anyRef: Any, property: KProperty<*>) = value
}
