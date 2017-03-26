package br.com.xy.inc.service

import br.com.xy.inc.service.beans.ModelBean
import br.com.xy.inc.service.beans.ProjectResultBean
import br.com.xy.inc.utils.builder.ApiBuilder
import br.com.xy.inc.utils.template.ProjectBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(path = arrayOf("/api"), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
open class ApiBuilderResource {

    @Autowired
    lateinit var apiBuilder: ApiBuilder

    @PostMapping("/project")
    fun createNewProject(@Valid @RequestBody project: ProjectBean) : ProjectResultBean {
        val createProjectResult = ProjectResultBean()

        if (!apiBuilder.isProjectExists(project.name)) {
            apiBuilder.createProject(project)
            createProjectResult.success = true
        } else {
            createProjectResult.success = false
            createProjectResult.message = "Project already exists."
        }

        return createProjectResult
    }

    @PostMapping("/model")
    fun createNewEntity(@Valid @RequestBody model: ModelBean) : ProjectResultBean {
        val createProjectResult = ProjectResultBean()

        if (!apiBuilder.isProjectExists(model.projectName)) {
            createProjectResult.success = false
            createProjectResult.message = "Project not found."
        } else if (apiBuilder.isEntityExists(model.projectName, model.entity.name)) {
            createProjectResult.success = false
            createProjectResult.message = "Entity already exists."
        } else {
            apiBuilder.createEntity(model.projectName, model.entity)
            createProjectResult.success = true
        }

        return createProjectResult
    }

}