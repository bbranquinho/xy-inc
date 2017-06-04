package br.com.xy.inc.test.builder

import br.com.xy.inc.test.builder.utils.BaseTest
import br.com.xy.inc.utils.ApplicationProperties
import br.com.xy.inc.utils.builder.ApiBuilder
import br.com.xy.inc.utils.template.FieldBean
import br.com.xy.inc.utils.template.ModelBean
import br.com.xy.inc.utils.template.ProjectBean
import br.com.xy.inc.utils.template.TypeFieldBean
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import kotlin.test.assertTrue

open class ProjectBuilderTest : BaseTest() {

    @Autowired
    private lateinit var builder: ApiBuilder

    @Autowired
    lateinit var applicationProperties: ApplicationProperties

    @Test
    fun testProjectBuilderProject() {
        val project = ProjectBean(
                name = "xy-inc-example",
                port = 8081,
                basePackage = "br.com.xy.inc.example",
                group = "br.com.xy.inc",
                version = "0.0.1-SNAPSHOT",
                databaseName = "xy_inc_example",
                databaseUsername = "root",
                databasePassword = "root")

        builder.createProject(project)

        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/kotlin/br/com/xy/inc/example/Application.kt").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/kotlin/br/com/xy/inc/example/utils/ApplicationConfig.kt").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/kotlin/br/com/xy/inc/example/utils/BaseEntity.kt").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/kotlin/br/com/xy/inc/example/utils/GenericResource.kt").exists())

        assertTrue(File("${applicationProperties.projectPath}/${project.name}/build.gradle").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/gradle.properties").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/README.md").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/settings.gradle").exists())

        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/resources/application.yml").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/resources/application-dev.yml").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/resources/application-prod.yml").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/webapp/WEB-INF/jboss-web.xml").exists())
    }

    @Test
    fun testProjectBuilderEntity() {
        val project = ProjectBean(
                name = "xy-inc-example",
                port = 8081,
                basePackage = "br.com.xy.inc.example",
                group = "br.com.xy.inc",
                version = "0.0.1-SNAPSHOT",
                databaseName = "xy_inc_example",
                databaseUsername = "root",
                databasePassword = "root")

        val fields = arrayListOf<FieldBean>(
                FieldBean("name", TypeFieldBean.STRING),
                FieldBean("description", TypeFieldBean.STRING),
                FieldBean("price", TypeFieldBean.DECIMAL),
                FieldBean("category", TypeFieldBean.STRING),
                FieldBean("date", TypeFieldBean.DATETIME),
                FieldBean("quantity", TypeFieldBean.FLOAT))

        val entity = ModelBean(name = "product", tableName = "tb_product", fields = fields)

        builder.createModel(project, entity)

        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/kotlin/br/com/xy/inc/example/product/ProductEntity.kt").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/kotlin/br/com/xy/inc/example/product/ProductRepository.kt").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/kotlin/br/com/xy/inc/example/product/ProductResource.kt").exists())
    }

}
