package br.com.xy.inc.test.builder.properties

import br.com.xy.inc.builder.properties.PairProperty
import br.com.xy.inc.builder.utils.PropertyReplacer
import br.com.xy.inc.test.builder.utils.BaseTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

open class PropertyReplacerFieldTest: BaseTest() {

    @Autowired
    lateinit var propertyReplacer: PropertyReplacer

    @Test
    fun testPropertyReplacerFieldInteger() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("name", "description"))
        properties.add(PairProperty("column", "description"))
        properties.add(PairProperty("type", "Integer"))
        properties.add(PairProperty("defaultValue", "0"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/entity/Field.xyi")

        assertEquals(fileText,
"""    @Column(name = "description", unique = false, nullable = false, length = 255)
    var description: Integer = 0""")
    }

    @Test
    fun testPropertyReplacerFieldIntegerNullableTrue() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("name", "description"))
        properties.add(PairProperty("column", "description"))
        properties.add(PairProperty("type", "Integer"))
        properties.add(PairProperty("nullable", "true"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/entity/Field.xyi")

        assertEquals(fileText,
"""    @Column(name = "description", unique = false, nullable = true, length = 255)
    var description: Integer? = null""")
    }

    @Test
    fun testPropertyReplacerFieldIntegerNullableFalse() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("name", "description"))
        properties.add(PairProperty("column", "description"))
        properties.add(PairProperty("type", "Integer"))
        properties.add(PairProperty("defaultValue", "0"))
        properties.add(PairProperty("nullable", "false"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/entity/Field.xyi")

        assertEquals(fileText,
"""    @Column(name = "description", unique = false, nullable = false, length = 255)
    var description: Integer = 0""")
    }

    @Test
    fun testPropertyReplacerFieldIntegerUniqueTrue() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("name", "description"))
        properties.add(PairProperty("column", "description"))
        properties.add(PairProperty("type", "Integer"))
        properties.add(PairProperty("defaultValue", "0"))
        properties.add(PairProperty("unique", "true"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/entity/Field.xyi")

        assertEquals(fileText,
"""    @Column(name = "description", unique = true, nullable = false, length = 255)
    var description: Integer = 0""")
    }

    @Test
    fun testPropertyReplacerFieldIntegerUniqueFalse() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("name", "description"))
        properties.add(PairProperty("column", "description"))
        properties.add(PairProperty("type", "Integer"))
        properties.add(PairProperty("defaultValue", "0"))
        properties.add(PairProperty("unique", "false"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/entity/Field.xyi")

        assertEquals(fileText,
"""    @Column(name = "description", unique = false, nullable = false, length = 255)
    var description: Integer = 0""")
    }

    @Test
    fun testPropertyReplacerFieldIntegerLength() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("name", "description"))
        properties.add(PairProperty("column", "description"))
        properties.add(PairProperty("type", "Integer"))
        properties.add(PairProperty("defaultValue", "0"))
        properties.add(PairProperty("length", "10"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/entity/Field.xyi")

        assertEquals(fileText,
"""    @Column(name = "description", unique = false, nullable = false, length = 10)
    var description: Integer = 0""")
    }

}
