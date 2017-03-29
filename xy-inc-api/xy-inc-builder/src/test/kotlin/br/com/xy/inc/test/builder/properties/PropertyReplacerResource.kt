package br.com.xy.inc.test.builder.properties

import br.com.xy.inc.builder.properties.PairProperty
import br.com.xy.inc.builder.utils.PropertyReplacer
import br.com.xy.inc.test.builder.utils.BaseTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import kotlin.test.assertEquals

open class PropertyReplacerResource: BaseTest() {

    @Autowired
    lateinit var propertyReplacer: PropertyReplacer

    @Test
    fun testPropertyReplacerResourceApplication() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("projectName", "xy-inc"))
        properties.add(PairProperty("projectPort", "8081"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/src/main/resources/application.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerResourceApplication.test").readText())
    }

    @Test
    fun testPropertyReplacerResourceApplicationDev() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("projectName", "xy-inc"))
        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("databaseName", "xy_inc"))
        properties.add(PairProperty("databaseUsername", "root"))
        properties.add(PairProperty("databasePassword", "root"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/src/main/resources/application-dev.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerResourceApplicationDev.test").readText())
    }

    @Test
    fun testPropertyReplacerResourceApplicationProd() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("projectName", "xy-inc"))
        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("databaseName", "xy_inc"))
        properties.add(PairProperty("databaseUsername", "root"))
        properties.add(PairProperty("databasePassword", "root"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/src/main/resources/application-prod.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerResourceApplicationProd.test").readText())
    }

    @Test
    fun testPropertyReplacerResourceApplicationJBossWeb() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("projectName", "xy-inc"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/src/main/webapp/WEB-INF/jboss-web.xyi")

        assertEquals(fileText,  File(System.getProperty("user.dir") + "/src/test/resources/templates/testPropertyReplacerResourceApplicationJBossWeb.test").readText())
    }

}