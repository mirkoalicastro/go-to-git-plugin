package com.mirkoalicastro.gotogit.safe

import com.mirkoalicastro.gotogit.log.Logging
import com.mirkoalicastro.gotogit.log.logger
import kotlin.reflect.KProperty

fun <T> tryLazy(initializer: () -> T?) = TryLazy(initializer)

class TryLazy<T>(initializer: () -> T?): Logging {
    private val value: T? by lazy {
        try {
            initializer()
        } catch (e: Exception) {
            logger().error("Failed to initialize.", e)
            null
        }
    }

    operator fun getValue(anyRef: Any, property: KProperty<*>) = value
}
