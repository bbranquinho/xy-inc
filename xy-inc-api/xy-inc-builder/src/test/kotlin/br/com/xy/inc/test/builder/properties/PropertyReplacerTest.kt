package br.com.xy.inc.test.builder.properties

import br.com.xy.inc.builder.properties.PairProperty
import br.com.xy.inc.builder.utils.PropertyReplacer
import br.com.xy.inc.test.builder.utils.BaseTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

open class PropertyReplacerTest: BaseTest() {

    @Autowired
    lateinit var propertyReplacer: PropertyReplacer

    @Test
    fun testPropertyReplacerEntity() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("entitySimpleName", "product"))
        properties.add(PairProperty("entityName", "ProductEntity"))
        properties.add(PairProperty("tableName", "tb_product"))
        properties.add(PairProperty("keyName", "pk"))
        properties.add(PairProperty("keyType", "Long"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/entity/Entity.xyi")

        assertEquals(fileText,
"""package br.com.xy.inc.product

import br.com.xy.inc.utils.BaseEntity
import java.math.BigDecimal
import java.util.*

import javax.persistence.AttributeOverride
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "tb_product")
@AttributeOverride(name = "id", column = Column(name = "pk"))
open class ProductEntity : BaseEntity<Long>() {
@field@
}
""")
    }

    @Test
    fun testPropertyReplacerResource() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("entitySimpleName", "product"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/entity/Resource.xyi")

        assertEquals(fileText,
"""package br.com.xy.inc.product

import br.com.xy.inc.utils.GenericResource
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = arrayOf("/api/product"))
open class ProductResource : GenericResource<ProductEntity, Long>() {

    // @resourceSearch@

}
""")
    }

    @Test
    fun testPropertyReplacerRepository() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("entitySimpleName", "product"))
        properties.add(PairProperty("entityName", "ProductEntity"))
        properties.add(PairProperty("repositoryName", "ProductRepository"))
        properties.add(PairProperty("keyType", "Long"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/entity/Repository.xyi")

        assertEquals(fileText,
"""package br.com.xy.inc.product

import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<ProductEntity, Long> {

    // @repositorySearch@

}
""")
    }

    @Test
    fun testPropertyReplacerApplication() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/Application.xyi")

        assertEquals(fileText,
"""package br.com.xy.inc

import br.com.xy.inc.utils.ApplicationConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.support.SpringBootServletInitializer

@SpringBootApplication
open class Application : SpringBootServletInitializer() {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(ApplicationConfig::class.java, *args)
        }
    }

}
""")
    }

    @Test
    fun testPropertyReplacerBaseEntity() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/BaseEntity.xyi")

        assertEquals(fileText,
"""package br.com.xy.inc.utils

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
""")
    }

    @Test
    fun testPropertyReplacerApplicationConfig() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("projectVersion", "0.0.1"))
        properties.add(PairProperty("projectName", "xy-inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/ApplicationConfig.xyi")

        assertEquals(fileText,
"""package br.com.xy.inc.utils

import br.com.xy.inc.Application
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = arrayOf(Application::class))
open class ApplicationConfig {

    @Bean
    open fun newsApi() = Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().paths(regex("/api/.*")).build()

    fun apiInfo() = ApiInfoBuilder().title("xy-inc").description("Swagger API").version("0.0.1").build()

}
""")
    }

    @Test
    fun testPropertyReplacerGenericResource() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/project/GenericResource.xyi")

        assertEquals(fileText,
"""package br.com.xy.inc.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.Serializable
import java.util.*

@RequestMapping(produces = arrayOf(MediaType.APPLICATION_JSON_VALUE), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
abstract class GenericResource<T: BaseEntity<K>, K: Serializable> {

    @Autowired
    lateinit var repository: JpaRepository<T, K>

    @GetMapping("/{id}")
    fun findById(@PathVariable(value = "id") id: K) =
        Optional.ofNullable(repository.findOne(id))
                .map{ r -> ResponseEntity<T>(r, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))

    @GetMapping
    fun findAll() = repository.findAll()

    @PostMapping
    fun add(@RequestBody entityObject: T) = repository.save(entityObject)

    @PutMapping
    fun update(@RequestBody entityObject: T) =
        if (entityObject.id == null)
            ResponseEntity<T>(HttpStatus.NOT_FOUND)
        else
            ResponseEntity<T>(repository.save(entityObject),  HttpStatus.OK)

    @DeleteMapping
    fun delete(@RequestBody entityObject: T) = repository.delete(entityObject)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") id: K) = repository.delete(id)

}
""")
    }

    @Test
    fun testPropertyReplacerSettingsGradle() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("projectName", "xy-inc"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/structure/settings.gradle.xyi")

        assertEquals(fileText,
"""rootProject.name = 'xy-inc'
""")
    }

    @Test
    fun testPropertyReplacerGradleProperties() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("projectVersion", "0.0.1"))

        val fileText = propertyReplacer.replaceProperties(properties, "/templates/structure/gradle.properties.xyi")

        assertEquals(fileText,
"""xyIncGroup=br.com.xy.inc
xyIncVersion=0.0.1
kotlinVersion=1.1.1
""")
    }

}
