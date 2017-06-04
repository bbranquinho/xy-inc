package br.com.xy.inc.builder.properties

import br.com.xy.inc.builder.utils.PropertyReplacer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PropertyReplacerImpl: PropertyReplacer {

    private val logger: Logger = LoggerFactory.getLogger(PropertyReplacerImpl::class.java)

    private val delimiter = "@"

    override fun replaceProperties(properties: List<PairProperty>, pathClasspath: String): String {
        val fileStream = javaClass.getResourceAsStream(pathClasspath)

        var fileText = fileStream.reader().readText()

        logger.debug("Reading the file {}", fileText)

        properties.forEach{ fileText = fileText.replace(delimiter + it.token + delimiter, it.value) }

        logger.debug("Property of the file {} replaced {}", pathClasspath, fileText)

        return fileText
    }

    override fun replaceFieldProperties(properties: List<PairProperty>, pathClasspath: String): String {
        val fieldProperties = ArrayList<PairProperty>()

        if (!properties.any{ it.token.equals("unique") }) {
            fieldProperties.add(PairProperty("unique", "false"))
        }

        if (!properties.any{ it.token.equals("length") }) {
            fieldProperties.add(PairProperty("length", "255"))
        }

        if (properties.any{ it.token.equals("nullable") && it.value.equals("true") }) {
            fieldProperties.add(PairProperty("isNullableField", "?"))
            fieldProperties.add(PairProperty("defaultValue", "null"))
        } else {
            fieldProperties.add(PairProperty("nullable", "false"))
            fieldProperties.add(PairProperty("isNullableField", ""))
        }

        return replaceProperties(properties + fieldProperties, pathClasspath)
    }

}