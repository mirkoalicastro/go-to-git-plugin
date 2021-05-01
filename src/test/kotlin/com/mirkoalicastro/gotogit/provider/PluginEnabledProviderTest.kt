package com.mirkoalicastro.gotogit.provider

import com.intellij.openapi.actionSystem.AnActionEvent
import com.mirkoalicastro.gotogit.browser.Browser
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk

class PluginEnabledProviderTest : StringSpec({
    val browser: Browser = mockk()
    val repoUrlProvider: RepoUrlProvider = mockk()
    val event: AnActionEvent = mockk()
    val underTest = PluginEnabledProvider(browser, repoUrlProvider)

    afterTest {
        clearAllMocks()
    }

    "should return false when URL is not defined" {
        every { repoUrlProvider.provide(event) } returns null

        val actual = underTest.provide(event)

        actual shouldBe false
    }

    "should return false when URL is defined but browser is not supported" {
        every { repoUrlProvider.provide(event) } returns "https://github.com"
        every { browser.isBrowsable() } returns false

        val actual = underTest.provide(event)

        actual shouldBe false
    }

    "should return true when URL is defined and browser is supported" {
        every { repoUrlProvider.provide(event) } returns "https://github.com"
        every { browser.isBrowsable() } returns true

        val actual = underTest.provide(event)

        actual shouldBe true
    }
})
