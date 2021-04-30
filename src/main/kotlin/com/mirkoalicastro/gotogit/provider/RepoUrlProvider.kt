package com.mirkoalicastro.gotogit.provider

import com.intellij.openapi.actionSystem.AnActionEvent
import com.mirkoalicastro.gotogit.git.Repo

class RepoUrlProvider {
    fun provide(e: AnActionEvent) = getRepo(e)?.getRepoUrl()

    private fun getRepo(e: AnActionEvent) =
        e.project?.basePath?.let {
            Repo(it)
        }
}
