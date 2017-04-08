package br.com.xy.inc.utils.template

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

open class FieldBean {

    constructor()

    constructor(name: String, type: TypeFieldBean) {
        this.name = name;
        this.type = type;
    }

    @NotNull
    @Size(min = 4)
    var name = ""

    @NotNull
    var type = TypeFieldBean.STRING

}