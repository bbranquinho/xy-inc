package br.com.xy.inc.utils.template

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

open class FieldBean constructor(name: String, type: TypeFieldBean) {

    @NotNull
    @Size(min = 4)
    val name = name

    @NotNull
    val type = type

}