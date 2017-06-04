package br.com.xy.inc.builder

import br.com.xy.inc.builder.properties.PairProperty
import br.com.xy.inc.builder.utils.PropertyReplacer
import br.com.xy.inc.utils.ApplicationProperties
import br.com.xy.inc.utils.builder.ApiBuilder
import br.com.xy.inc.utils.builder.ApiManager
import br.com.xy.inc.utils.jsonToObject
import br.com.xy.inc.utils.template.ModelBean
import br.com.xy.inc.utils.template.ProjectBean
import br.com.xy.inc.utils.template.TypeFieldBean
import br.com.xy.inc.utils.writeJsonToFile
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File

@Component
class ApiBuilderImpl @Autowired constructor(val propertyReplacer: PropertyReplacer, var applicationProperties: ApplicationProperties) : ApiBuilder {

    private val logger = LoggerFactory.getLogger(ApiBuilderImpl::class.java)

    @Autowired
    private lateinit var apiManager: ApiManager

    override fun isProjectExists(projectName: String) =
            File("${applicationProperties.projectPath}/${projectName}").exists()

    override fun isModelExists(projectName: String, modelName: String) =
            File("${applicationProperties.projectPath}/${projectName}/.xyi/${modelName.toLowerCase()}.json").exists()

    override fun createModel(projectName: String, model: ModelBean) =
            createModel(File("${applicationProperties.projectPath}/${projectName}/.xyi/project.json").jsonToObject(ProjectBean::class.java), model)

    override fun getAllProjects(): List<ProjectBean> {
        val projectFilePath = File(applicationProperties.projectPath)
        val projects = ArrayList<ProjectBean>()

        if (projectFilePath.exists()) {
            projectFilePath.listFiles { f -> f.isDirectory }.forEach {
                val project = File("${it.absolutePath}/.xyi/project.json")

                if (project.exists() && project.isFile()) {
                    projects.add(project.jsonToObject(ProjectBean::class.java))
                } else {
                    logger.error("Project not found [{}].", project.absolutePath)
                }
            }
        }

        return projects
    }

    override fun getProject(projectName: String): ProjectBean? {
        val project = File("${applicationProperties.projectPath}/${projectName}/.xyi/project.json")

        return if (project.exists() && project.isFile())
            project.jsonToObject(ProjectBean::class.java)
        else
            null
    }

    override fun getModelsByProject(projectName: String): List<ModelBean>? {
        val entities = ArrayList<ModelBean>()
        val project = File("${applicationProperties.projectPath}/${projectName}/.xyi")

        if (!project.exists()) {
            return null
        }

        project.listFiles { f -> f.isFile && !f.name.equals("project.json") }.forEach {
            entities.add(it.jsonToObject(ModelBean::class.java))
        }

        return entities
    }

    override fun getModelByProject(projectName: String, modelName: String): ModelBean? {
        val project = File("${applicationProperties.projectPath}/${projectName}/.xyi/${modelName}.json")

        return if (project.exists() && project.isFile())
            project.jsonToObject(ModelBean::class.java)
        else
            null
    }

    override fun createProject(project: ProjectBean) {
        val file = File(applicationProperties.projectPath)

        if (!file.exists()) {
            logger.info("Creating the project folder [{}].", applicationProperties.projectPath)
            file.mkdir()
        }

        val projectFilePath = File("${applicationProperties.projectPath}/${project.name}")

        if (!projectFilePath.exists()) {
            projectFilePath.mkdir()
        }

        logger.info("Creating the project folder [{}].", projectFilePath.absolutePath)
        projectFilePath.mkdir()
        createPackageFolders(projectFilePath, project)

        saveProperties(project, "project.json", projectFilePath)

        if (applicationProperties.isStartApiAutomatically) {
            apiManager.startApi(project.name)
        }
    }

    override fun createModel(project: ProjectBean, model: ModelBean) {
        val projectFilePath = File("${applicationProperties.projectPath}/${project.name}")

        if (!projectFilePath.exists()) {
            throw Exception("Project [${project.name}] must exist.")
        }

        if (applicationProperties.isStartApiAutomatically) {
            apiManager.stopApi(project.name)
        }

        var fieldsCode = ""

        model.fields.forEach {
            val fieldProperties = arrayListOf<PairProperty>(
                    PairProperty("type", it.type.type),
                    PairProperty("name", it.name),
                    PairProperty("column", it.name),
                    PairProperty("nullable", "true"),
                    PairProperty("unique", "false"),
                    PairProperty("length", "255")
            )

            fieldsCode += "\n" + propertyReplacer.replaceFieldProperties(fieldProperties, "/templates/entity/src/main/kotlin/entity/Field.kt.xyi") + "\n"
        }

        val modelFolder = "${projectFilePath.absolutePath}/src/main/kotlin/${project.basePackage.replace(".", "/")}/${model.name}"
        val modelSimpleName = model.name[0].toUpperCase() + model.name.substring(1)

        val modelProperties = arrayListOf<PairProperty>(
                PairProperty("tableName", model.tableName),
                PairProperty("entityName", "${modelSimpleName}Entity"),
                PairProperty("keyType", TypeFieldBean.LONG.type),
                PairProperty("field", fieldsCode),
                PairProperty("packageName", project.basePackage),
                PairProperty("entitySimpleName", modelSimpleName.toLowerCase()),
                PairProperty("keyName", "id"),
                PairProperty("repositoryName", "${modelSimpleName}Repository"),
                PairProperty("resourceName", "${modelSimpleName}Resource")
        )

        saveCode(modelProperties, "/templates/entity/src/main/kotlin/entity/@entityName@Entity.kt.xyi", modelFolder, "${modelSimpleName}Entity.kt")
        saveCode(modelProperties, "/templates/entity/src/main/kotlin/entity/@entityName@Repository.kt.xyi", modelFolder, "${modelSimpleName}Repository.kt")
        saveCode(modelProperties, "/templates/entity/src/main/kotlin/entity/@entityName@Resource.kt.xyi", modelFolder, "${modelSimpleName}Resource.kt")

        saveProperties(model, "${model.name}.json", projectFilePath)

        if (applicationProperties.isStartApiAutomatically) {
            apiManager.startApi(project.name)
        }
    }

    private fun createPackageFolders(projectFilePath: File, project: ProjectBean) {
        val projectFolders = "${projectFilePath.absolutePath}/src/main/kotlin/${project.basePackage.replace(".", "/")}"
        val projectResourceFolders = "${projectFilePath.absolutePath}/src/main/resources"

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

        saveCode(projectProperties, "/templates/project/src/main/kotlin/Application.kt.xyi", projectFolders, "Application.kt")
        saveCode(projectProperties, "/templates/project/src/main/kotlin/utils/ApplicationConfig.kt.xyi", "${projectFolders}/utils", "ApplicationConfig.kt")
        saveCode(projectProperties, "/templates/project/src/main/kotlin/utils/BaseEntity.kt.xyi", "${projectFolders}/utils", "BaseEntity.kt")
        saveCode(projectProperties, "/templates/project/src/main/kotlin/utils/GenericResource.kt.xyi", "${projectFolders}/utils", "GenericResource.kt")
        saveCode(projectProperties, "/templates/project/src/main/kotlin/utils/PaginationUtil.kt.xyi", "${projectFolders}/utils", "PaginationUtil.kt")

        saveCode(projectProperties, "/templates/project/build.gradle.xyi", projectFilePath.absolutePath, "build.gradle")
        saveCode(projectProperties, "/templates/project/gradle.properties.xyi", projectFilePath.absolutePath, "gradle.properties")
        saveCode(projectProperties, "/templates/project/README.md.xyi", projectFilePath.absolutePath, "README.md")
        saveCode(projectProperties, "/templates/project/settings.gradle.xyi", projectFilePath.absolutePath, "settings.gradle")

        saveCode(projectProperties, "/templates/project/src/main/resources/application.yml.xyi", projectResourceFolders, "application.yml")
        saveCode(projectProperties, "/templates/project/src/main/resources/application-dev.yml.xyi", projectResourceFolders, "application-dev.yml")
        saveCode(projectProperties, "/templates/project/src/main/resources/application-prod.yml.xyi", projectResourceFolders, "application-prod.yml")
        saveCode(projectProperties, "/templates/project/src/main/webapp/WEB-INF/jboss-web.xml.xyi", "${projectResourceFolders}/../webapp/WEB-INF", "jboss-web.xml")
    }

    private fun saveCode(properties: List<PairProperty>, xyiFile: String, projectFolder: String, codeFilename: String) {
        logger.debug("Creating the file [{}] inside the folder [{}].", codeFilename, projectFolder)

        val code = propertyReplacer.replaceProperties(properties, xyiFile)

        File(projectFolder).mkdirs()

        val codeFile = File("${projectFolder}/${codeFilename}")

        if (!codeFile.exists() && !codeFile.createNewFile()) {
            throw Exception("Error to create the file [${codeFile.absolutePath}].")
        }

        codeFile.writeText(code)
    }

    private fun saveProperties(project: Any, filename: String, projectFilePath: File) {
        val xyiFolder = File("${projectFilePath.absolutePath}/.xyi")

        if (!xyiFolder.exists()) {
            xyiFolder.mkdir()
        }

        File("${xyiFolder.absolutePath}/${filename.toLowerCase()}").writeJsonToFile(project)
    }

}
