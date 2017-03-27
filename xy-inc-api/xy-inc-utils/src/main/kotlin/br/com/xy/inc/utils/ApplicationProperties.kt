package br.com.xy.inc.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
open class ApplicationProperties {

    @Value("\${app.project.path}")
    var projectPath: String? = null

    @Value("\${app.api.log.size:#{null}}")
    var apiLogSize: Int? = null

}