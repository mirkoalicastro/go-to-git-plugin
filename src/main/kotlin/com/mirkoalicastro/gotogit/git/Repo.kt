package com.mirkoalicastro.gotogit.git

import com.mirkoalicastro.gotogit.log.Logging
import com.mirkoalicastro.gotogit.log.logger
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File
import java.io.IOException

class Repo(path: String) : Logging {
    private val fileRepository = tryOrNull {
        FileRepositoryBuilder().setWorkTree(File(path)).build()
    }

    fun getRemoteUrl() = fileRepository?.config?.getString("remote", "origin", "url")

    private fun <T> tryOrNull(initializer: () -> T?) =
        try {
            initializer()
        } catch (e: IOException) {
            logger().error("Failed to initialize.", e)
            null
        }
}
