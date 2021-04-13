package com.github.mirkoalicastro.gitshortcutplugin.services

import com.github.mirkoalicastro.gitshortcutplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
