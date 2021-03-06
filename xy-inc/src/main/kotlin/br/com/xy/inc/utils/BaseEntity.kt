package br.com.xy.inc.utils

import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.springframework.data.jpa.domain.AbstractPersistable
import java.io.Serializable

abstract class BaseEntity<K: Serializable> : AbstractPersistable<K>() {

    override fun toString() = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE)

    override fun equals(other: Any?) = EqualsBuilder.reflectionEquals(this, other)

    public override fun setId(id: K) = super.setId(id)

    @JsonIgnore
    override fun isNew() = super.isNew()
}
