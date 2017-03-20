package br.com.test.xy.inc

import br.com.test.xy.inc.utils.BaseTest
import br.com.xy.inc.product.ProductEntity
import br.com.xy.inc.product.ProductRepository
import br.com.xy.inc.product.ProductResource
import org.junit.Assert
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import java.math.BigDecimal
import java.util.*

open class ProductResourceTest: BaseTest() {

    val log: Logger = LoggerFactory.getLogger(ProductResourceTest::class.java)

    @Autowired
    lateinit var resource: ProductResource

    @Autowired
    lateinit var repository: ProductRepository

    @Test
    fun testGetAllSuccess() {
        val products = resource.findAll()

        log.debug("Products: {}", products)

        Assert.assertNotNull(products)
        Assert.assertTrue(!products.isEmpty())
    }

    @Test
    fun testFindByIdSuccess() {
        var product = ProductEntity().apply {
            name = "Name 1"
            description = "Description 1"
            price = BigDecimal("10.300")
            category = "Category 2"
        }

        product = repository.save(product)

        var resourceProduct = resource.findById(product.id).body

        Assert.assertEquals(product, resourceProduct)
    }

    @Test
    fun testFindByIdFailure() {
        val maxId = repository.findAll().maxBy(ProductEntity::getId)?.id

        val product = if (maxId == null) resource.findById(0L).body else resource.findById(maxId + 1L).body

        Assert.assertNull(product)
    }

    @Test
    fun testAddSuccess() {
        var product = ProductEntity().apply {
            name = "Name 2"
            description = "Description 2"
            price = BigDecimal("10.300")
            category = "Category 2"
        }

        val newProduct = resource.add(product)

        product.id = newProduct.id

        Assert.assertEquals(product, newProduct)
    }

    @Test
    fun testUpdateSuccess() {
        val product = repository.findAll(PageRequest(0, 1)).first()

        product.name = UUID.randomUUID().toString()

        val newProduct = resource.update(product).body

        Assert.assertEquals(product, newProduct)
    }

    @Test
    fun testUpdateFailure() {
        val product = ProductEntity()

        Assert.assertNull(resource.update(product).body)
    }

    @Test
    fun testDeleteSuccess() {
        var product = ProductEntity()

        product = repository.save(product)

        resource.delete(product)

        Assert.assertNull(repository.findOne(product.id))
    }

}