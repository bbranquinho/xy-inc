package br.com.xy.inc.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

private val jacksonObjectMapper = jacksonObjectMapper()

fun File.writeJsonToFile(obj: Any) =
        jacksonObjectMapper.writerWithDefaultPrettyPrinter().writeValue(this, obj)

fun <T> File.jsonToObject(t: Class<T>): T =
        jacksonObjectMapper.readValue(this, t)

fun <T> String.jsonToObject(t: Class<T>): T =
        jacksonObjectMapper.readValue(this, t)

fun <T> T.objectToJson(): String =
        jacksonObjectMapper.writeValueAsString(this)

