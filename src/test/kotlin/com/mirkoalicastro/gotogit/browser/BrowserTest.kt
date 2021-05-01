package com.mirkoalicastro.gotogit.browser

import com.mirkoalicastro.gotogit.safe.TryLazy
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.verify
import java.awt.Desktop
import java.net.URI
import java.net.URISyntaxException
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class BrowserTest : StringSpec({
    val desktop: Desktop = mockk()

    afterTest {
        clearAllMocks()
    }

    "Should retrieve the Desktop from AWT" {
        mockkStatic(Desktop::class)
        every { Desktop.getDesktop() } returns desktop
        val browser = Browser()
        val field = Browser::class.memberProperties
            .firstOrNull { it.name == "desktop" }
            ?.apply { isAccessible = true }

        val actual = field?.get(browser)

        actual shouldBe desktop
        verify(exactly = 1) { Desktop.getDesktop() }
    }

    "isBrowsable should delegate to Desktop" {
        table(
            headers("desktop", "supported"),
            row(desktop, true),
            row(desktop, false)
        ).forAll { desktop, supported ->
            injectDesktop(desktop)

            every { desktop.isSupported(Desktop.Action.BROWSE) } returns supported

            val actual = Browser().isBrowsable()

            actual shouldBe supported
        }
    }

    "isBrowsable should return false when desktop is null" {
        injectDesktop(null)

        val actual = Browser().isBrowsable()

        actual shouldBe false
    }

    "Should not throw any exception when desktop is null" {
        injectDesktop(null)

        Browser().browse("https://github.com/")
    }

    "Should not throw any exception when uri is invalid" {
        injectDesktop(desktop)
        val invalidUri = ":"

        Browser().browse(invalidUri)

        shouldThrow<URISyntaxException> { URI(invalidUri) }
    }

    "Should not throw any exception when browse throws exception" {
        injectDesktop(desktop)
        val uri = "https://github.com"
        every { desktop.browse(URI(uri)) } throws RuntimeException()

        Browser().browse(uri)

        verify(exactly = 1) { desktop.browse(URI(uri)) }
    }
})

private fun injectDesktop(desktop: Desktop?) {
    mockkConstructor(TryLazy::class)
    every { anyConstructed<TryLazy<Desktop>>().getValue(any(), any()) } returns desktop
}