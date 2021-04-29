package com.mirkoalicastro.gotogit.browser

import com.mirkoalicastro.gotogit.log.Logging
import com.mirkoalicastro.gotogit.safe.tryLazy
import com.mirkoalicastro.gotogit.safe.tryWith
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
}
