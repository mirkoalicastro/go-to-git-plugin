package com.mirkoalicastro.gotogit

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Presentation
import com.mirkoalicastro.gotogit.browser.Browser
import com.mirkoalicastro.gotogit.provider.PluginEnabledProvider
import com.mirkoalicastro.gotogit.provider.RepoUrlProvider
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class GotoGitActionTest : StringSpec({
    val browser: Browser = mockk()
    val repoUrlProvider: RepoUrlProvider = mockk()
    val pluginEnabledProvider: PluginEnabledProvider = mockk()
    val event: AnActionEvent = mockk()
    val presentation: Presentation = mockk()

    val underTest = createUnderTest(browser, repoUrlProvider, pluginEnabledProvider)

    afterTest {
        clearAllMocks()
    }

    "actionPerformed should do nothing when URL is null" {
        every { repoUrlProvider.provide(event) } returns null

        underTest.actionPerformed(event)

        verify { repoUrlProvider.provide(event) }
        confirmVerified(repoUrlProvider)
    }

    "actionPerformed should call browse when URL is defined" {
        val uri = "https://github.com"
        every { repoUrlProvider.provide(event) } returns uri
        justRun { browser.browse(uri) }

        underTest.actionPerformed(event)

        verify { browser.browse(uri) }
        confirmVerified(browser)
    }

    "update should enable to presentation according to the provider" {
        table(
            headers("enabled"),
            row(true),
            row(false)
        ).forAll { supported ->
            every { event.presentation } returns presentation
            every { pluginEnabledProvider.provide(event) } returns supported
            justRun { presentation.isEnabled = supported }

            underTest.update(event)

            verify {
                presentation.isEnabled = supported
            }
            confirmVerified(presentation)
        }
    }
})

private fun createUnderTest(
    browser: Browser,
    urlProvider: RepoUrlProvider,
    enabledProvider: PluginEnabledProvider
) = GotoGitAction().apply {
    overrideField("browser", browser)
    overrideField("repoUrlProvider", urlProvider)
    overrideField("pluginEnabledProvider", enabledProvider)
}

private fun GotoGitAction.overrideField(name: String, value: Any) {
    val property = GotoGitAction::class.java.declaredFields
        .firstOrNull { it.name == name }
        ?.apply { isAccessible = true }

    property?.set(this, value)
}
