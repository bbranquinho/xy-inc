package br.com.xy.inc.test.builder

import br.com.xy.inc.test.builder.utils.BaseTest
import br.com.xy.inc.utils.ApplicationProperties
import br.com.xy.inc.utils.builder.ApiBuilder
import br.com.xy.inc.utils.template.EntityBean
import br.com.xy.inc.utils.template.FieldBean
import br.com.xy.inc.utils.template.ProjectBean
import br.com.xy.inc.utils.template.TypeFieldBean
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import kotlin.test.assertTrue

open class ProjectBuilderTest : BaseTest() {

    @Autowired
    lateinit var builder: ApiBuilder

    @Autowired
    lateinit var applicationProperties: ApplicationProperties

    @Test
    fun testProjectBuilderProject() {
        val project = ProjectBean()

        project.name = "xy-inc-example"
        project.port = 8081
        project.basePackage = "br.com.xy.inc.example"
        project.group = "br.com.xy.inc"
        project.version = "0.0.1-SNAPSHOT"
        project.databaseName = "xy_inc_example"
        project.databaseUsername = "root"
        project.databasePassword = "root"

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
        val project = ProjectBean()

        project.name = "xy-inc-example"
        project.port = 8081
        project.basePackage = "br.com.xy.inc.example"
        project.group = "br.com.xy.inc"
        project.version = "0.0.1-SNAPSHOT"
        project.databaseName = "xy_inc_example"
        project.databaseUsername = "root"
        project.databasePassword = "root"

        val entity = EntityBean()
        entity.name = "product"
        entity.tableName = "tb_product"

        entity.fields.add(FieldBean("name", TypeFieldBean.STRING))
        entity.fields.add(FieldBean("description", TypeFieldBean.STRING))
        entity.fields.add(FieldBean("price", TypeFieldBean.DECIMAL))
        entity.fields.add(FieldBean("category", TypeFieldBean.STRING))
        entity.fields.add(FieldBean("date", TypeFieldBean.DATETIME))
        entity.fields.add(FieldBean("quantity", TypeFieldBean.FLOAT))

        builder.createEntity(project, entity)

        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/kotlin/br/com/xy/inc/example/product/ProductEntity.kt").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/kotlin/br/com/xy/inc/example/product/ProductRepository.kt").exists())
        assertTrue(File("${applicationProperties.projectPath}/${project.name}/src/main/kotlin/br/com/xy/inc/example/product/ProductResource.kt").exists())
    }

}
