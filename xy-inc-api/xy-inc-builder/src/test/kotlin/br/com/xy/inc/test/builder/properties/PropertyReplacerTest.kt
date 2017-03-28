package br.com.xy.inc.test.builder.properties

import br.com.xy.inc.builder.properties.PairProperty
import br.com.xy.inc.builder.utils.PropertyReplacer
import br.com.xy.inc.test.builder.utils.BaseTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import kotlin.test.assertEquals

open class PropertyReplacerTest: BaseTest() {

    @Autowired
    lateinit var propertyReplacer: PropertyReplacer

    @Test
    fun testPropertyReplacerEntity() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("entitySimpleName", "product"))
        properties.add(PairProperty("entityName", "ProductEntity"))
        properties.add(PairProperty("tableName", "tb_product"))
        properties.add(PairProperty("keyName", "pk"))
        properties.add(PairProperty("keyType", "Long"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/entity/Entity.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerEntity.test").readText())
    }

    @Test
    fun testPropertyReplacerResource() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("entitySimpleName", "product"))
        properties.add(PairProperty("entityName", "ProductEntity"))
        properties.add(PairProperty("resourceName", "ProductResource"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/entity/Resource.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerResource.test").readText())
    }

    @Test
    fun testPropertyReplacerRepository() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("entitySimpleName", "product"))
        properties.add(PairProperty("entityName", "ProductEntity"))
        properties.add(PairProperty("repositoryName", "ProductRepository"))
        properties.add(PairProperty("keyType", "Long"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/entity/Repository.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerRepository.test").readText())
    }

    @Test
    fun testPropertyReplacerApplication() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/Application.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerApplication.test").readText())
    }

    @Test
    fun testPropertyReplacerBaseEntity() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/BaseEntity.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerBaseEntity.test").readText())
    }

    @Test
    fun testPropertyReplacerApplicationConfig() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("projectVersion", "0.0.1"))
        properties.add(PairProperty("projectName", "xy-inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/ApplicationConfig.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerApplicationConfig.test").readText())
    }

    @Test
    fun testPropertyReplacerGenericResource() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/GenericResource.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerGenericResource.test").readText())
    }

    @Test
    fun testPropertyReplacerSettingsGradle() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("projectName", "xy-inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/structure/settings.gradle.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerSettingsGradle.test").readText())
    }

    @Test
    fun testPropertyReplacerGradleProperties() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("projectVersion", "0.0.1"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/structure/gradle.properties.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerGradleProperties.test").readText())
    }

    @Test
    fun testPropertyReplacerPaginationUtil() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/PaginationUtil.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerPaginationUtil.test").readText())
    }

}
