package br.com.xy.inc.utils.template

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ProjectBean(
        @NotNull @Size(min = 4) val name: String,
        @NotNull @Size(min = 2) val port: Int,
        @NotNull @Size(min = 4) val basePackage: String,
        @NotNull @Size(min = 4) val group: String,
        @NotNull @Size(min = 1) val version: String,
        @NotNull @Size(min = 4) val databaseName: String,
        @NotNull val databaseUsername: String,
        @NotNull val databasePassword: String)
