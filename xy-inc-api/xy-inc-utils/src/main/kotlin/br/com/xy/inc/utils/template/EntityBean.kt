package br.com.xy.inc.utils.template

import br.com.xy.inc.utils.BaseBean

data class EntityBean(var name: String = "", var tableName: String = "", var fields: ArrayList<FieldBean> = arrayListOf())
    : BaseBean()
