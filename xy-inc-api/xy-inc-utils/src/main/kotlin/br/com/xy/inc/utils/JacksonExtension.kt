package br.com.xy.inc.utils


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

private object JacksonExtension {

    val jacksonObjectMapper: ObjectMapper =
            jacksonObjectMapper()

}

fun File.writeJsonToFile(obj: Any) =
        JacksonExtension.jacksonObjectMapper.writerWithDefaultPrettyPrinter().writeValue(this, obj)

fun <T> File.jsonToObject(t: Class<T>): T =
        JacksonExtension.jacksonObjectMapper.readValue(this, t)
