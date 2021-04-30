package com.mirkoalicastro.gotogit.git

import com.mirkoalicastro.gotogit.log.Logging
import com.mirkoalicastro.gotogit.log.logger
import com.mirkoalicastro.gotogit.safe.tryOrNull
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

class Repo(path: String) : Logging {

    companion object {
        val sshRegex = Regex("git@(.+):(.+).git")
    }

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

    private fun constructHttpsUrl(sshUrl: String) =
        sshRegex.matchEntire(sshUrl)?.groupValues?.let {
            val host = it[1]
            val repo = it[2]
            "https://$host/$repo/"
        }.also {
            logger().debug("Constructed https url '$it' from ssh url '$sshUrl'")
        }

    private fun getRemoteUrl() = fileRepository?.config?.getString("remote", "origin", "url")
}
