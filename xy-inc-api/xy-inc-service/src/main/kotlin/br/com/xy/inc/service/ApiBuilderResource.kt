package br.com.xy.inc.service

import br.com.xy.inc.service.beans.ProjectResultBean
import br.com.xy.inc.service.utils.BaseResource
import br.com.xy.inc.utils.builder.ApiBuilder
import br.com.xy.inc.utils.template.ModelBean
import br.com.xy.inc.utils.template.ProjectBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/api/project"])
class ApiBuilderResource @Autowired constructor(val apiBuilder: ApiBuilder): BaseResource {

    @GetMapping(consumes = [MediaType.ALL_VALUE])
    fun listProjects() =
            apiBuilder.getAllProjects()

    @GetMapping("/{projectName}", consumes = [MediaType.ALL_VALUE])
    fun findProject(@PathVariable("projectName") projectName: String) =
            Optional.ofNullable(apiBuilder.getProject(projectName))
                    .map{ r -> ResponseEntity<ProjectBean>(r, HttpStatus.OK) }
                    .orElse(ResponseEntity(HttpStatus.NOT_FOUND))


    @GetMapping("/{projectName}/model", consumes = [MediaType.ALL_VALUE])
    fun findModelsByProject(@PathVariable("projectName") projectName: String) =
            Optional.ofNullable(apiBuilder.getModelsByProject(projectName))
                    .map{ r -> ResponseEntity<List<ModelBean>>(r, HttpStatus.OK) }
                    .orElse(ResponseEntity(HttpStatus.NOT_FOUND))

    @GetMapping("/{projectName}/model/{modelName}", consumes = [MediaType.ALL_VALUE])
    fun findModelByProject(@PathVariable("projectName") projectName: String, @PathVariable("modelName") modelName: String) =
            Optional.ofNullable(apiBuilder.getModelByProject(projectName, modelName))
                    .map{ r -> ResponseEntity<ModelBean>(r, HttpStatus.OK) }
                    .orElse(ResponseEntity(HttpStatus.NOT_FOUND))

    @PostMapping
    fun createNewProject(@Valid @RequestBody project: ProjectBean) =
        if (!apiBuilder.isProjectExists(project.name)) {
            apiBuilder.createProject(project)
            ProjectResultBean(true, "")
        } else {
            ProjectResultBean(false, "Project already exists.")
        }

    @PostMapping("/{projectName}/model")
    fun createNewModel(projectName: String, @Valid @RequestBody model: ModelBean) =
        if (!apiBuilder.isProjectExists(projectName)) {
            ProjectResultBean(false, "Project not found.")
        } else if (apiBuilder.isModelExists(projectName, model.name)) {
            ProjectResultBean(false, "Model already exists.")
        } else {
            apiBuilder.createModel(projectName, model)
            ProjectResultBean(true, "")
        }

}