package br.com.xy.inc.product

import br.com.xy.inc.utils.GenericResource
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = arrayOf("/api/product"))
open class ProductResource : GenericResource<ProductEntity, Long>() {

    override fun getBaseUrl() = "/api/product"

}
