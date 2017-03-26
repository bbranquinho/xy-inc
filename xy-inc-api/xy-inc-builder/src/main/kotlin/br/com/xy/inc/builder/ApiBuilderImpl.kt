package br.com.xy.inc.builder

import br.com.xy.inc.builder.properties.PairProperty
import br.com.xy.inc.builder.utils.PropertyReplacer
import br.com.xy.inc.utils.ApplicationProperties
import br.com.xy.inc.utils.builder.ApiBuilder
import br.com.xy.inc.utils.template.EntityBean
import br.com.xy.inc.utils.template.ProjectBean
import br.com.xy.inc.utils.template.TypeFieldBean
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File

@Component
open class ApiBuilderImpl : ApiBuilder {

    val logger = LoggerFactory.getLogger(ApiBuilderImpl::class.java)

    @Autowired
    lateinit var propertyReplacer: PropertyReplacer

    @Autowired
    lateinit var applicationProperties: ApplicationProperties

    @Autowired
    lateinit var mapper: ObjectMapper

    override fun isProjectExists(projectName: String): Boolean {
        return File(applicationProperties.projectPath + "/" + projectName).exists()
    }

    override fun isEntityExists(projectName: String, entityName: String): Boolean {
        return File(applicationProperties.projectPath + "/" + projectName + "/.xyi/" + entityName.toLowerCase() + ".json").exists()
    }

    override fun createEntity(projectName: String, entity: EntityBean) {
        val  project = mapper.readValue(File(applicationProperties.projectPath + "/" + projectName), ProjectBean::class.java)

        createEntity(project, entity)
    }

    override fun getAllProjects(): List<ProjectBean> {
        val projectFilePath = File(applicationProperties.projectPath)
        val projects = ArrayList<ProjectBean>()

        projectFilePath.listFiles{ f -> f.isDirectory }.forEach {
            val project = File(it.absolutePath + "/.xyi/project.json")

            if (project.exists() && project.isFile()) {
                projects.add(mapper.readValue(project, ProjectBean::class.java))
            } else {
                logger.error("Project not found [%s]".format(project.absolutePath))
            }
        }

        return projects
    }

    override fun getProject(projectName: String): ProjectBean? {
        val project = File(applicationProperties.projectPath + "/" + projectName + "/.xyi/project.json")

        if (project.exists() && project.isFile()) {
            return mapper.readValue(project, ProjectBean::class.java)
        } else {
            return null
        }
    }

    override fun getEntitiyByProject(projectName: String, entityName: String): EntityBean? {
        val project = File(applicationProperties.projectPath + "/" + projectName + "/.xyi/" + entityName + ".json")

        if (project.exists() && project.isFile()) {
            return mapper.readValue(project, EntityBean::class.java)
        } else {
            return null
        }
    }

    override fun getEntitiesByProject(projectName: String): List<EntityBean>? {
        val entities = ArrayList<EntityBean>()
        val project = File(applicationProperties.projectPath + "/" + projectName + "/.xyi")

        if (!project.exists()) {
            return null
        }

        project.listFiles { f -> f.isFile && !f.name.equals("project.json") }.forEach {
            entities.add(mapper.readValue(it, EntityBean::class.java))
        }

        return entities
    }

    override fun createProject(projectTemplate: ProjectBean) {
        val file = File(applicationProperties.projectPath)

        if (!file.exists()) {
            logger.info("Creating the project folder [%s]".format(applicationProperties.projectPath))
            file.mkdir()
        }

        val projectFilePath = File(applicationProperties.projectPath + "/" + projectTemplate.name)

        if (!projectFilePath.exists()) {
            projectFilePath.mkdir()
        }

        logger.info("Creating the project folder [%s]".format(projectFilePath.absolutePath))
        projectFilePath.mkdir()
        createPackafeFolders(projectFilePath, projectTemplate)

        saveProperties(projectTemplate, "project.json", projectFilePath)
    }

    override fun createEntity(projectTemplate: ProjectBean, entity: EntityBean) {
        val projectFilePath = File(applicationProperties.projectPath + "/" + projectTemplate.name)

        if (!projectFilePath.exists()) {
            throw Exception("Project must exist [%s]".format(projectTemplate.name))
        }

        var fieldsCode = ""

        entity.fields.forEach {
            val fieldProperties = arrayListOf<PairProperty>(
                    PairProperty("type", it.type.type),
                    PairProperty("name", it.name),
                    PairProperty("column", it.name),
                    PairProperty("nullable", "true"),
                    PairProperty("unique", "false"),
                    PairProperty("length", "255")
            )

            fieldsCode += "\n" + propertyReplacer.replaceFieldProperties(fieldProperties, "/templates/entity/Field.xyi") + "\n"
        }

        val entityFolder = projectFilePath.absolutePath + "/src/main/kotlin/" + projectTemplate.basePackage.replace(".", "/") + "/" + entity.name
        val entitySimpleName = entity.name[0].toUpperCase() + entity.name.substring(1)

        val entityProperties = arrayListOf<PairProperty>(
                PairProperty("tableName", entity.name),
                PairProperty("entityName", entitySimpleName + "Entity"),
                PairProperty("keyType", TypeFieldBean.LONG.type),
                PairProperty("field", fieldsCode),
                PairProperty("packageName", projectTemplate.basePackage),
                PairProperty("entitySimpleName", entitySimpleName.toLowerCase()),
                PairProperty("keyName", "id"),
                PairProperty("repositoryName", entitySimpleName + "Repository")
        )

        saveCode(entityProperties, "/templates/entity/Entity.xyi", entityFolder, entitySimpleName + "Entity.kt")
        saveCode(entityProperties, "/templates/entity/Repository.xyi", entityFolder, entitySimpleName + "Repository.kt")
        saveCode(entityProperties, "/templates/entity/Resource.xyi", entityFolder, entitySimpleName + "Resource.kt")

        saveProperties(entity, entity.name + ".json", projectFilePath)
    }

    private fun createPackafeFolders(projectFilePath: File, project: ProjectBean) {
        val projectFolders = projectFilePath.absolutePath + "/src/main/kotlin/" + project.basePackage.replace(".", "/")
        val projectResourceFolders = projectFilePath.absolutePath + "/src/main/resources"

        File(projectFolders).mkdirs()
        File(projectResourceFolders).mkdirs()

        val projectProperties = arrayListOf<PairProperty>(
            PairProperty("packageName", project.basePackage),
            PairProperty("projectVersion", project.version),
            PairProperty("projectName", project.name),
            PairProperty("projectPort", project.port.toString()),
            PairProperty("databaseName", project.databaseName),
            PairProperty("databaseUsername", project.databaseUsername),
            PairProperty("databasePassword", project.databasePassword)
        )

        saveCode(projectProperties, "/templates/project/Application.xyi", projectFolders, "Application.kt")
        saveCode(projectProperties, "/templates/project/ApplicationConfig.xyi", projectFolders + "/utils", "ApplicationConfig.kt")
        saveCode(projectProperties, "/templates/project/BaseEntity.xyi", projectFolders + "/utils", "BaseEntity.kt")
        saveCode(projectProperties, "/templates/project/GenericResource.xyi", projectFolders + "/utils", "GenericResource.kt")

        saveCode(projectProperties, "/templates/structure/build.gradle.xyi", projectFilePath.absolutePath, "build.gradle")
        saveCode(projectProperties, "/templates/structure/gradle.properties.xyi", projectFilePath.absolutePath, "gradle.properties")
        saveCode(projectProperties, "/templates/structure/README.md.xyi", projectFilePath.absolutePath, "README.md")
        saveCode(projectProperties, "/templates/structure/settings.gradle.xyi", projectFilePath.absolutePath, "settings.gradle")

        saveCode(projectProperties, "/templates/resources/application.xyi", projectResourceFolders, "application.yml")
        saveCode(projectProperties, "/templates/resources/application-dev.xyi", projectResourceFolders, "application-dev.yml")
        saveCode(projectProperties, "/templates/resources/application-prod.xyi", projectResourceFolders, "application-prod.yml")
        saveCode(projectProperties, "/templates/resources/jboss-web.xyi", projectResourceFolders + "/../webapp/WEB-INF", "jboss-web.xml")
    }

    private fun saveCode(properties: List<PairProperty>, xyiFile: String, projectFolder: String, codeFilename: String) {
        logger.debug("Creating the file [%s] inside the folder [%s].".format(codeFilename, projectFolder))

        val code = propertyReplacer.replaceProperties(properties, xyiFile)

        File(projectFolder).mkdirs()

        val codeFile = File(projectFolder + "/" + codeFilename)

        if (!codeFile.exists() && !codeFile.createNewFile()) {
            throw Exception("Error to create the file [%s]".format(codeFile.absolutePath))
        }

        codeFile.writeText(code)
    }

    private fun saveProperties(projectTemplate: Any, filename: String, projectFilePath: File) {
        val xyiFolder = File(projectFilePath.absolutePath + "/.xyi")

        if (!xyiFolder.exists()) {
            xyiFolder.mkdir()
        }

        val xyiProjectFile = File(xyiFolder.absolutePath + "/" + filename.toLowerCase())

        mapper.writerWithDefaultPrettyPrinter().writeValue(xyiProjectFile, projectTemplate)
    }

}
