package com.mirkoalicastro.gotogit

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.mirkoalicastro.gotogit.browser.Browser
import com.mirkoalicastro.gotogit.log.Logging
import com.mirkoalicastro.gotogit.log.logger
import com.mirkoalicastro.gotogit.provider.PluginEnabledProvider
import com.mirkoalicastro.gotogit.provider.RepoUrlProvider

class GotoGitAction : AnAction(), Logging {
    private val browser = Browser()
    private val repoUrlProvider = RepoUrlProvider()
    private val pluginEnabledProvider = PluginEnabledProvider(browser, repoUrlProvider)

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = pluginEnabledProvider.provide(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        repoUrlProvider.provide(e)?.run {
            logger().debug("Browsing to $this")
            browser.browse(this)
        }
    }
}
