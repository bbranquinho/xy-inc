package br.com.xy.inc.utils.template

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

open class EntityBean {

    @NotNull
    @Size(min = 4)
    var name = String()

    var tableName: String? = null

    var fields = ArrayList<FieldBean>()

}
