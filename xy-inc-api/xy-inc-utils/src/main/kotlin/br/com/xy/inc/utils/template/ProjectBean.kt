package br.com.xy.inc.utils.template

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


open class ProjectBean {

    @NotNull
    @Size(min = 4)
    var name = ""

    @NotNull
    var port = 8080

    @NotNull
    @Size(min = 4)
    var basePackage = ""

    @NotNull
    @Size(min = 4)
    var group = ""

    @NotNull
    @Size(min = 1)
    var version = ""

    @NotNull
    @Size(min = 4)
    var databaseName = ""

    @NotNull
    var databaseUsername = ""

    @NotNull
    var databasePassword = ""

    // TODO augusto.branquinho: Add support to multiple databases.
    //var databaseType = DatabaseType.H2DB

}
