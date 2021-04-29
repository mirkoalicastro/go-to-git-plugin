package com.mirkoalicastro.gotogit.git

import com.mirkoalicastro.gotogit.log.Logging
import com.mirkoalicastro.gotogit.safe.tryOrNull
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

class Repo(path: String) : Logging {
    private val fileRepository = tryOrNull {
        FileRepositoryBuilder().setWorkTree(File(path)).build()
    }

    fun getRemoteUrl() = fileRepository?.config?.getString("remote", "origin", "url")
}
