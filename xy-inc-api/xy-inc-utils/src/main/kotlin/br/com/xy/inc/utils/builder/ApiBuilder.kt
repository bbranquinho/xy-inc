package br.com.xy.inc.utils.builder

import br.com.xy.inc.utils.template.ModelBean
import br.com.xy.inc.utils.template.ProjectBean

interface ApiBuilder {

    fun isProjectExists(projectName: String): Boolean

    fun isModelExists(projectName: String, modelName: String): Boolean

    fun getAllProjects(): List<ProjectBean>

    fun getProject(projectName: String): ProjectBean?

    fun getModelsByProject(projectName: String): List<ModelBean>?

    fun getModelByProject(projectName: String, modelName: String): ModelBean?

    fun createProject(project: ProjectBean)

    fun createModel(projectName: String, model: ModelBean)

    fun createModel(project: ProjectBean, model: ModelBean)

}
