package br.com.xy.inc.utils.template

import br.com.xy.inc.utils.BaseBean
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ProjectBean(
        @NotNull @Size(min = 4) var name: String = "",
        @NotNull @Size(min = 2) var port: Int = 8080,
        @NotNull @Size(min = 4) var basePackage: String = "",
        @NotNull @Size(min = 4) var group: String = "",
        @NotNull @Size(min = 1) var version: String = "",
        @NotNull @Size(min = 4) var databaseName: String = "",
        @NotNull var databaseUsername: String = "",
        @NotNull var databasePassword: String = "")
    : BaseBean()
