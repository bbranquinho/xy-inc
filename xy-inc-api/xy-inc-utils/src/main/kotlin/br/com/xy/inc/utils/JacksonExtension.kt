package br.com.xy.inc.utils

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

private object JacksonExtension {

    val jacksonObjectMapper = ObjectMapper()

}

fun File.writeJsonToFile(obj: Any) =
        JacksonExtension.jacksonObjectMapper.writerWithDefaultPrettyPrinter().writeValue(this, obj)

fun <T> File.jsonToObject(t: Class<T>): T =
        JacksonExtension.jacksonObjectMapper.readValue(this, t)

fun <T> String.jsonToObject(t: Class<T>): T =
        JacksonExtension.jacksonObjectMapper.readValue(this, t)

fun <T> T.objectToJson(): String =
        JacksonExtension.jacksonObjectMapper.writeValueAsString(this)

