package br.com.xy.inc.utils.template

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ProjectBean(
        @field:[NotNull Size(min = 4)] val name: String,
        @field:[NotNull Min(value = 8000)] val port: Int,
        @field:[NotNull Size(min = 4)] val basePackage: String,
        @field:[NotNull Size(min = 4)] val group: String,
        @field:[NotNull Size(min = 1)] val version: String,
        @field:[NotNull Size(min = 4)] val databaseName: String,
        @field:[NotNull] val databaseUsername: String,
        @field:[NotNull] val databasePassword: String)
