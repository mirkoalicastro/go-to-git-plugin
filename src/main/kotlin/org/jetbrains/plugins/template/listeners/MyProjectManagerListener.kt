package org.jetbrains.plugins.template.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.jetbrains.plugins.template.services.MyProjectService
import java.io.File


internal class MyProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.service<MyProjectService>()
        println("Project opened")
        val repo = getFileRepository(project)
        println("Config: ${repo?.config?.toText()}")
    }

    private fun getFileRepository(project: Project) =
        try {
            project.basePath?.let {
                FileRepositoryBuilder()
                    .setWorkTree(File(it))
                    .build()
            }
        } catch (e: Exception) {
            println("Got exception while getting FileRepository: $e")
            null
        }
}
