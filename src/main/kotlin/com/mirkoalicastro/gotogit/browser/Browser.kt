package com.mirkoalicastro.gotogit.browser

import com.mirkoalicastro.gotogit.log.Logging
import com.mirkoalicastro.gotogit.log.logger
import com.mirkoalicastro.gotogit.safe.tryLazy
import java.awt.Desktop
import java.net.URI

class Browser : Logging {
    private val desktop: Desktop? by tryLazy {
        Desktop.getDesktop()
    }

    fun isBrowsable() =
        desktop?.isSupported(Desktop.Action.BROWSE) ?: false

    fun browse(uri: String) = tryWith(desktop) {
        browse(URI(uri))
    }

    private fun <T> tryWith(obj: T?, block: T.() -> Unit) {
        if (obj != null) {
            try {
                block(obj)
            } catch (e: Exception) {
                logger().error("Failed to browse.", e)
            }
        }
    }
}
