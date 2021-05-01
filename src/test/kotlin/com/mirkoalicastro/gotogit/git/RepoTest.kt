package com.mirkoalicastro.gotogit.git

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
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.storage.file.FileBasedConfig
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

class RepoTest : StringSpec({
    val fileRepoBuilder: FileRepositoryBuilder = mockk()
    val fileRepo: FileRepository = mockk()
    val config: FileBasedConfig = mockk()

    afterTest {
        clearAllMocks()
    }

    "should return null when FileRepository#setWorkTree throws Exception" {
        val path = "path"
        mockkConstructor(FileRepositoryBuilder::class)
        every { anyConstructed<FileRepositoryBuilder>().setWorkTree(File(path)) } throws Exception()

        val actual = Repo(path).getRepoUrl()

        actual shouldBe null
    }

    "should return null when FileRepository#build throws Exception" {
        val path = "path"
        mockkConstructor(FileRepositoryBuilder::class)
        every { anyConstructed<FileRepositoryBuilder>().setWorkTree(File(path)) } returns fileRepoBuilder
        every { fileRepoBuilder.build() } throws Exception()

        val actual = Repo(path).getRepoUrl()

        actual shouldBe null
    }

    "should return null when FileRepository is null" {
        val path = "path"
        mockkConstructor(FileRepositoryBuilder::class)
        every { anyConstructed<FileRepositoryBuilder>().setWorkTree(File(path)) } returns fileRepoBuilder
        every { fileRepoBuilder.build() } returns null

        val actual = Repo(path).getRepoUrl()

        actual shouldBe null
    }

    "should return null when StoredConfig is null" {
        val path = "path"
        mockkConstructor(FileRepositoryBuilder::class)
        every { anyConstructed<FileRepositoryBuilder>().setWorkTree(File(path)) } returns fileRepoBuilder
        every { fileRepoBuilder.build() } returns fileRepo
        every { fileRepo.config } returns null

        val actual = Repo(path).getRepoUrl()

        actual shouldBe null
    }

    "should return null when RemoteOriginUrl is null" {
        val path = "path"
        mockkConstructor(FileRepositoryBuilder::class)
        every { anyConstructed<FileRepositoryBuilder>().setWorkTree(File(path)) } returns fileRepoBuilder
        every { fileRepoBuilder.build() } returns fileRepo
        every { fileRepo.config } returns config
        every { config.getString("remote", "origin", "url") } returns null

        val actual = Repo(path).getRepoUrl()

        actual shouldBe null
    }

    "should return repository URL when config is retrieved" {
        table(
            headers("remoteOriginUrl", "expected"),
            row("http://github.com/mirkoalicastro/", "http://github.com/mirkoalicastro/"),
            row("https://github.com/mirkoalicastro/", "https://github.com/mirkoalicastro/"),
            row("git@github.com:mirkoalicastro/test.git", "https://github.com/mirkoalicastro/test/"),
            row("git@github.com:.git", null),
            row("git@:test.git", null),
            row("git@", null),
            row("ftp://github.com", null)
        ).forAll { remoteOriginUrl, expected ->
            val path = "path"
            mockkConstructor(FileRepositoryBuilder::class)
            every { anyConstructed<FileRepositoryBuilder>().setWorkTree(File(path)) } returns fileRepoBuilder
            every { fileRepoBuilder.build() } returns fileRepo
            every { fileRepo.config } returns config
            every { config.getString("remote", "origin", "url") } returns remoteOriginUrl

            val actual = Repo(path).getRepoUrl()

            actual shouldBe expected
        }
    }
})
