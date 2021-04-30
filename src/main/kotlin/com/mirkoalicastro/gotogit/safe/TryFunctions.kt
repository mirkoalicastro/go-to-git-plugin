package com.mirkoalicastro.gotogit.safe

import com.mirkoalicastro.gotogit.log.Logging
import com.mirkoalicastro.gotogit.log.logger

fun <T> Logging.tryOrNull(initializer: () -> T?) =
    try {
        initializer()
    } catch (e: Exception) {
        logger().error("Failed to execute.", e)
        null
    }

fun <T> Logging.tryWith(obj: T?, block: T.() -> Unit) {
    if (obj != null) {
        try {
            block(obj)
        } catch (e: Exception) {
            logger().error("Failed to execute.", e)
        }
    }
}
