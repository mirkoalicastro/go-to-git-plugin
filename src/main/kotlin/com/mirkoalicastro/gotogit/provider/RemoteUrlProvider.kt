package com.mirkoalicastro.gotogit.provider

import com.intellij.openapi.actionSystem.AnActionEvent
import com.mirkoalicastro.gotogit.git.Repo

class RemoteUrlProvider {
    fun provide(e: AnActionEvent) = getRepo(e)?.getRemoteUrl()

    private fun getRepo(e: AnActionEvent) =
        e.project?.basePath?.let {
            Repo(it)
        }
}
