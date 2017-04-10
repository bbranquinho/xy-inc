package br.com.xy.inc.service

import br.com.xy.inc.service.beans.ModelBean
import br.com.xy.inc.service.beans.ProjectResultBean
import br.com.xy.inc.service.utils.BaseResource
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
@RequestMapping(path = arrayOf("/api/project"))
open class ApiBuilderResource: BaseResource {

    @Autowired
    lateinit var apiBuilder: ApiBuilder

    @GetMapping(consumes = arrayOf(MediaType.ALL_VALUE))
    fun listProjects() = apiBuilder.getAllProjects()

    @GetMapping("/{projectName}", consumes = arrayOf(MediaType.ALL_VALUE))
    fun findProject(@PathVariable("projectName") projectName: String) =
            Optional.ofNullable(apiBuilder.getProject(projectName))
                    .map{ r -> ResponseEntity<ProjectBean>(r, HttpStatus.OK) }
                    .orElse(ResponseEntity(HttpStatus.NOT_FOUND))


    @GetMapping("/{projectName}/entities", consumes = arrayOf(MediaType.ALL_VALUE))
    fun findEntitiesByProject(@PathVariable("projectName") projectName: String) =
            Optional.ofNullable(apiBuilder.getEntitiesByProject(projectName))
                    .map{ r -> ResponseEntity<List<EntityBean>>(r, HttpStatus.OK) }
                    .orElse(ResponseEntity(HttpStatus.NOT_FOUND))

    @GetMapping("/{projectName}/entities/{entityName}", consumes = arrayOf(MediaType.ALL_VALUE))
    fun findEntitiyByProject(@PathVariable("projectName") projectName: String, @PathVariable("entityName") entityName: String) =
            Optional.ofNullable(apiBuilder.getEntitiyByProject(projectName, entityName))
                    .map{ r -> ResponseEntity<EntityBean>(r, HttpStatus.OK) }
                    .orElse(ResponseEntity(HttpStatus.NOT_FOUND))

    @PostMapping
    fun createNewProject(@Valid @RequestBody project: ProjectBean) =
        if (!apiBuilder.isProjectExists(project.name)) {
            apiBuilder.createProject(project)
            ProjectResultBean(true, "")
        } else {
            ProjectResultBean(false, "Project already exists.")
        }

    @PostMapping("/model")
    fun createNewEntity(@Valid @RequestBody model: ModelBean) =
        if (!apiBuilder.isProjectExists(model.projectName)) {
            ProjectResultBean(false, "Project not found.")
        } else if (apiBuilder.isEntityExists(model.projectName, model.entity.name)) {
            ProjectResultBean(false, "Entity already exists.")
        } else {
            apiBuilder.createEntity(model.projectName, model.entity)
            ProjectResultBean(true, "")
        }

}