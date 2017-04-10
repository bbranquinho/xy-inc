package br.com.xy.inc.utils.template

import br.com.xy.inc.utils.BaseBean
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ProjectBean (
        @NotNull @Size(min = 4) val name: String,
        @NotNull val port: String,
        @NotNull @Size(min = 4) val basePackage: String,
        @NotNull @Size(min = 4) val group: String,
        @NotNull @Size(min = 1) val version: String,
        @NotNull @Size(min = 4) val databaseName: String,
        @NotNull val databaseUsername: String,
        @NotNull val databasePassword: String)
    : BaseBean()
