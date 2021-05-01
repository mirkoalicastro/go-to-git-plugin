package com.mirkoalicastro.gotogit.safe

import com.mirkoalicastro.gotogit.log.Logging
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class TryFunctionsKtTest : StringSpec({
    val initializer: () -> String? = mockk()
    val block: (String) -> Unit = mockk()

    class UnderTest : Logging

    afterTest {
        clearAllMocks()
    }

    "tryOrNull should return lambda response if exception is not thrown" {
        val expected = "expected"
        every { initializer() } returns expected

        val actual = UnderTest().tryOrNull(initializer)

        actual shouldBe expected
        verify(exactly = 1) { initializer() }
    }

    "tryOrNull should return null if lambda threw exception" {
        every { initializer() } throws Exception()

        val actual = UnderTest().tryOrNull(initializer)

        actual shouldBe null
    }

    "tryWith should do nothing if object is null" {
        UnderTest().tryWith(null, block)

        verify(exactly = 0) { block(any()) }
    }

    "tryWith should not throw exception" {
        val receiver = "object"
        every { block(receiver) } throws Exception()

        UnderTest().tryWith(receiver, block)

        verify(exactly = 1) { block(receiver) }
    }

    "tryWith should call block on receiver" {
        val receiver = "object"
        justRun { block(receiver) }

        UnderTest().tryWith(receiver, block)

        verify(exactly = 1) { block(receiver) }
    }
})
