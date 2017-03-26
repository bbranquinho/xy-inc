package br.com.xy.inc.utils.builder

import br.com.xy.inc.utils.template.EntityBean
import br.com.xy.inc.utils.template.ProjectBean

interface ApiBuilder {

    fun isProjectExists(projectName: String): Boolean

    fun isEntityExists(projectName: String, entityName: String): Boolean

    fun createProject(projectTemplate: ProjectBean)

    fun createEntity(projectName: String, entity: EntityBean)

    fun createEntity(projectTemplate: ProjectBean, entity: EntityBean)

}
