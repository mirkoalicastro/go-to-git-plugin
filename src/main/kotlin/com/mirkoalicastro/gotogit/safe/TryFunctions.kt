package com.mirkoalicastro.gotogit.safe

import com.mirkoalicastro.gotogit.log.Logging
import com.mirkoalicastro.gotogit.log.logger

fun <T> tryLazy(initializer: () -> T?) = TryLazy(initializer)

fun <T> Logging.tryOrNull(initializer: () -> T?) =
    tryOrElse({
        initializer()
    }) {
        logger().error("Failed to execute.", it)
        initializer()
    }

fun <T> Logging.tryWith(obj: T?, block: T.() -> Unit) {
    if (obj != null) {
        tryOrElse({
            block(obj)
        }) {
            logger().error("Failed to execute.", it)
        }
    }
}

private fun <T, S : T> tryOrElse(initializer: () -> T?, fallback: (Exception) -> S) =
    try {
        initializer()
    } catch (e: Exception) {
        fallback(e)
    }
