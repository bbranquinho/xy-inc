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
        properties.add(PairProperty("entityName", "Product"))
        properties.add(PairProperty("tableName", "tb_product"))
        properties.add(PairProperty("keyName", "pk"))
        properties.add(PairProperty("keyType", "Long"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/entity/src/main/kotlin/entity/@entityName@Entity.kt.xyi")

        assertEquals(File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerEntity.test").readText(), fileText)
    }

    @Test
    fun testPropertyReplacerResource() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("entitySimpleName", "product"))
        properties.add(PairProperty("entityName", "Product"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/entity/src/main/kotlin/entity/@entityName@Resource.kt.xyi")

        assertEquals(File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerResource.test").readText(), fileText)
    }

    @Test
    fun testPropertyReplacerRepository() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("entitySimpleName", "product"))
        properties.add(PairProperty("entityName", "Product"))
        properties.add(PairProperty("keyType", "Long"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/entity/src/main/kotlin/entity/@entityName@Repository.kt.xyi")

        assertEquals(File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerRepository.test").readText(), fileText)
    }

    @Test
    fun testPropertyReplacerApplication() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/src/main/kotlin/Application.kt.xyi")

        assertEquals(File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerApplication.test").readText(), fileText)
    }

    @Test
    fun testPropertyReplacerBaseEntity() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/src/main/kotlin/utils/BaseEntity.kt.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerBaseEntity.test").readText())
    }

    @Test
    fun testPropertyReplacerApplicationConfig() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("projectVersion", "0.0.1"))
        properties.add(PairProperty("projectName", "xy-inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/src/main/kotlin/utils/ApplicationConfig.kt.xyi")

        assertEquals(File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerApplicationConfig.test").readText(), fileText)
    }

    @Test
    fun testPropertyReplacerGenericResource() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/src/main/kotlin/utils/GenericResource.kt.xyi")

        assertEquals(File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerGenericResource.test").readText(), fileText)
    }

    @Test
    fun testPropertyReplacerSettingsGradle() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("projectName", "xy-inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/settings.gradle.xyi")

        assertEquals(File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerSettingsGradle.test").readText(), fileText)
    }

    @Test
    fun testPropertyReplacerGradleProperties() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("projectVersion", "0.0.1"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/gradle.properties.xyi")

        assertEquals(File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerGradleProperties.test").readText(), fileText)
    }

    @Test
    fun testPropertyReplacerPaginationUtil() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/src/main/kotlin/utils/PaginationUtil.kt.xyi")

        assertEquals(File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerPaginationUtil.test").readText(), fileText)
    }

}
