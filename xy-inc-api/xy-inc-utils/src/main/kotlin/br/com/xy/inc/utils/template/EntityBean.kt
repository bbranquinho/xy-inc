package br.com.xy.inc.utils.template

import br.com.xy.inc.utils.BaseBean

data class EntityBean(val name: String, val tableName: String, val fields: ArrayList<FieldBean>)
    : BaseBean()
