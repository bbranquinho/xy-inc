package br.com.xy.inc.utils

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import java.io.Serializable

abstract class BaseBean: Serializable {

    override fun toString() = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE)

    override fun equals(other: Any?) = EqualsBuilder.reflectionEquals(this, other)

}
