package com.mirkoalicastro.gotogit.provider

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.mirkoalicastro.gotogit.git.Repo
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.EqMatcher
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor

class RepoUrlProviderTest : StringSpec({
    val event: AnActionEvent = mockk()
    val project: Project = mockk()

    afterTest {
        clearAllMocks()
    }

    "should return null when project is null" {
        every { event.project } returns null

        val actual = RepoUrlProvider().provide(event)

        actual shouldBe null
    }

    "should return null when project.basePath is null" {
        every { event.project } returns project
        every { project.basePath } returns null

        val actual = RepoUrlProvider().provide(event)

        actual shouldBe null
    }

    "should delegate to Repo" {
        val basePath = "base-path"
        val repoUrl = "repo-url"
        every { event.project } returns project
        every { project.basePath } returns basePath
        mockkConstructor(Repo::class)
        every { constructedWith<Repo>(EqMatcher(basePath)).getRepoUrl() } returns repoUrl

        val actual = RepoUrlProvider().provide(event)

        actual shouldBe repoUrl
    }
})
