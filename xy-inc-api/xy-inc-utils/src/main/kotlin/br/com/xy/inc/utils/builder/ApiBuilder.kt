package br.com.xy.inc.utils.builder

import br.com.xy.inc.utils.template.EntityBean
import br.com.xy.inc.utils.template.ProjectBean

interface ApiBuilder {

    fun isProjectExists(projectName: String): Boolean

    fun isEntityExists(projectName: String, entityName: String): Boolean

    fun getAllProjects(): List<ProjectBean>

    fun getProject(projectName: String): ProjectBean?

    fun getEntitiesByProject(projectName: String): List<EntityBean>?

    fun getEntitiyByProject(projectName: String, entityName: String): EntityBean?

    fun createProject(project: ProjectBean)

    fun createEntity(projectName: String, entity: EntityBean)

    fun createEntity(project: ProjectBean, entity: EntityBean)

}
