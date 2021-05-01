package com.mirkoalicastro.gotogit.safe

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.called
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class TryLazyTest : StringSpec({
    val initializer: () -> String = mockk()

    afterTest {
        clearAllMocks()
    }

    "Should not invoke initializer when getValue is not invoked " {
        TryLazy(initializer)

        verify {
            initializer() wasNot called
        }
    }

    "Should invoke initializer when getValue is invoked" {
        val expected = "expected"
        val underTest = TryLazy(initializer)
        every { initializer.invoke() } returns expected

        val actual = underTest.getValue(underTest, mockk())

        actual shouldBe expected
        verify { initializer.invoke() }
        confirmVerified(initializer)
    }

    "Should invoke initializer once" {
        val underTest = TryLazy(initializer)
        every { initializer.invoke() } returns ""

        repeat(3) {
            underTest.getValue(underTest, mockk())
        }

        verify(exactly = 1) { initializer.invoke() }
        confirmVerified(initializer)
    }

    "Should return null if initializer threw exception" {
        val underTest = TryLazy(initializer)
        every { initializer.invoke() } throws Exception()

        val actual = underTest.getValue(underTest, mockk())

        actual shouldBe null
    }
})
