package br.com.xy.inc.service

import br.com.xy.inc.service.beans.ModelBean
import br.com.xy.inc.service.beans.ProjectResultBean
import br.com.xy.inc.utils.builder.ApiBuilder
import br.com.xy.inc.utils.template.EntityBean
import br.com.xy.inc.utils.template.ProjectBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping(path = arrayOf("/api"), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
open class ApiBuilderResource {

    @Autowired
    lateinit var apiBuilder: ApiBuilder

    @GetMapping("/project", consumes = arrayOf(MediaType.ALL_VALUE))
    fun listProjects(): List<ProjectBean> {
        return apiBuilder.getAllProjects()
    }

    @GetMapping("/project/{projectName}", consumes = arrayOf(MediaType.ALL_VALUE))
    fun findProject(@PathVariable("projectName") projectName: String): ResponseEntity<ProjectBean> {
        return Optional.ofNullable(apiBuilder.getProject(projectName))
                .map{ r -> ResponseEntity<ProjectBean>(r, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping("/project/{projectName}/entities", consumes = arrayOf(MediaType.ALL_VALUE))
    fun findEntitiesByProject(@PathVariable("projectName") projectName: String): ResponseEntity<List<EntityBean>> {
        return Optional.ofNullable(apiBuilder.getEntitiesByProject(projectName))
                .map{ r -> ResponseEntity<List<EntityBean>>(r, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping("/project/{projectName}/entities/{entityName}", consumes = arrayOf(MediaType.ALL_VALUE))
    fun findEntitiyByProject(@PathVariable("projectName") projectName: String, @PathVariable("entityName") entityName: String): ResponseEntity<EntityBean> {
        return Optional.ofNullable(apiBuilder.getEntitiyByProject(projectName, entityName))
                .map{ r -> ResponseEntity<EntityBean>(r, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @PostMapping("/project")
    fun createNewProject(@Valid @RequestBody project: ProjectBean): ProjectResultBean {
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
    fun createNewEntity(@Valid @RequestBody model: ModelBean): ProjectResultBean {
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