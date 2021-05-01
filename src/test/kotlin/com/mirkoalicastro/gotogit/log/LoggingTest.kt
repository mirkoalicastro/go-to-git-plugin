package com.mirkoalicastro.gotogit.log

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.slf4j.LoggerFactory.getLogger

class LoggingTest : StringSpec({
    "Logging implementation should be able to get its logger" {
        class LoggingImpl : Logging
        val loggingImpl = LoggingImpl()

        val actual = loggingImpl.logger()

        actual shouldBe getLogger(LoggingImpl::class.java)
    }
})
