package br.com.xy.inc.product

import br.com.xy.inc.utils.BaseEntity
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "product")
open class ProductEntity : BaseEntity<Long>() {

    var name: String? = null

    var description: String? = null

    var price: BigDecimal? = null

    var category: String? = null

}
