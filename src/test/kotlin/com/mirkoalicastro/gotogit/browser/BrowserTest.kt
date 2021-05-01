package com.mirkoalicastro.gotogit.browser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import java.awt.Desktop
import java.net.URI
import java.net.URISyntaxException

class BrowserTest : StringSpec({
    val desktop: Desktop = mockk()

    beforeTest {
        mockkStatic(Desktop::class)
    }

    afterTest {
        verify(exactly = 1) { Desktop.getDesktop() }
        clearAllMocks()
    }

    "isBrowsable should delegate to Desktop" {
        every { Desktop.getDesktop() } returns desktop
        val browser = Browser()
        table(
            headers("supported"),
            row(true),
            row(false)
        ).forAll { supported ->
            every { desktop.isSupported(Desktop.Action.BROWSE) } returns supported

            val actual = browser.isBrowsable()

            actual shouldBe supported
        }
    }

    "isBrowsable should return false when desktop is null" {
        every { Desktop.getDesktop() } returns null

        val actual = Browser().isBrowsable()

        actual shouldBe false
    }

    "isBrowsable should return false when desktop threw exception" {
        every { Desktop.getDesktop() } throws Exception()

        val actual = Browser().isBrowsable()
        actual shouldBe false
    }

    "browse should not throw any exception when desktop threw exception" {
        every { Desktop.getDesktop() } throws Exception()

        Browser().browse("https://github.com/")
    }

    "browse should not throw any exception when desktop is null" {
        every { Desktop.getDesktop() } returns null

        Browser().browse("https://github.com/")
    }

    "browse should not throw any exception when uri is invalid" {
        every { Desktop.getDesktop() } returns desktop
        val invalidUri = ":"

        Browser().browse(invalidUri)

        shouldThrow<URISyntaxException> { URI(invalidUri) }
    }

    "browse should not throw any exception when desktop#browse throws exception" {
        every { Desktop.getDesktop() } returns desktop
        val uri = "https://github.com"
        every { desktop.browse(URI(uri)) } throws RuntimeException()

        Browser().browse(uri)

        verify(exactly = 1) { desktop.browse(URI(uri)) }
    }

    "browse should delegate to Desktop to browse" {
        every { Desktop.getDesktop() } returns desktop
        val uri = "https://github.com"
        justRun { desktop.browse(URI(uri)) }

        Browser().browse(uri)

        verify(exactly = 1) { desktop.browse(URI(uri)) }
    }
})
