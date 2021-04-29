package com.mirkoalicastro.gotogit

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.mirkoalicastro.gotogit.browser.Browser
import com.mirkoalicastro.gotogit.log.Logging
import com.mirkoalicastro.gotogit.log.logger
import com.mirkoalicastro.gotogit.provider.PluginEnabledProvider
import com.mirkoalicastro.gotogit.provider.RemoteUrlProvider

class GotoGitAction : AnAction(), Logging {
    private val browser = Browser()
    private val remoteUrlProvider = RemoteUrlProvider()
    private val pluginEnabledProvider = PluginEnabledProvider(browser, remoteUrlProvider)

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = pluginEnabledProvider.provide(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        remoteUrlProvider.provide(e)?.run {
            logger().debug("Redirecting to $this")
            browser.browse(this)
        }
    }
}
