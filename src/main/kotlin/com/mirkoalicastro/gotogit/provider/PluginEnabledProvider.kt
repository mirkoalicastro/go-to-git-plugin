package com.mirkoalicastro.gotogit.provider

import com.intellij.openapi.actionSystem.AnActionEvent
import com.mirkoalicastro.gotogit.browser.Browser

class PluginEnabledProvider(
    private val browser: Browser,
    private val repoUrlProvider: RepoUrlProvider
) {
    fun provide(e: AnActionEvent) = isUrlDefined(e) && browser.isBrowsable()

    private fun isUrlDefined(e: AnActionEvent) = repoUrlProvider.provide(e) != null
}
