package com.mirkoalicastro.gotogit.safe

import com.mirkoalicastro.gotogit.log.Logging
import kotlin.reflect.KProperty

class TryLazy<T>(initializer: () -> T?) : Logging {
    private val value by lazy {
        tryOrNull {
            initializer()
        }
    }

    operator fun getValue(anyRef: Any, property: KProperty<*>) = value
}
