package br.com.xy.inc.service.beans

import br.com.xy.inc.utils.BaseBean
import br.com.xy.inc.utils.template.EntityBean

data class ModelBean(var projectName: String, var entity: EntityBean)
    : BaseBean()
