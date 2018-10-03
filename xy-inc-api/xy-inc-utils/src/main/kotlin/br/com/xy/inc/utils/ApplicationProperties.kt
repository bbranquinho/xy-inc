package br.com.xy.inc.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
open class ApplicationProperties {

    @Value("\${app.project.path}")
    lateinit var projectPath: String

    @Value("\${app.api.log.size:#{0}}")
    var apiLogSize: Int = 0

    @Value("\${app.api.start.automatically:#{false}}")
    var isStartApiAutomatically: Boolean = false

}