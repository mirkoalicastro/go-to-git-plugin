package com.mirkoalicastro.gotogit.git

import com.mirkoalicastro.gotogit.log.Logging
import com.mirkoalicastro.gotogit.log.logger
import com.mirkoalicastro.gotogit.safe.tryOrNull
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

private val sshRegex = Regex("git@(.+):(.+).git")

class Repo(path: String) : Logging {
    private val fileRepository = tryOrNull {
        FileRepositoryBuilder().setWorkTree(File(path)).build()
    }

    fun getRepoUrl() =
        with(getRemoteUrl()) {
            when {
                this == null -> null
                startsWith("git@") -> constructHttpsUrl(this)
                startsWith("https://") || startsWith("http://") -> this
                else -> null
            }
        }

    private fun getRemoteUrl() = fileRepository?.config?.getString("remote", "origin", "url")

    private fun constructHttpsUrl(sshUrl: String) =
        sshRegex.matchEntire(sshUrl)?.let {
            val group = it.groupValues
            "https://${group[1]}/${group[2]}/"
        }.also {
            logger().debug("Constructed https url '$it' from ssh url '$sshUrl'")
        }
}
