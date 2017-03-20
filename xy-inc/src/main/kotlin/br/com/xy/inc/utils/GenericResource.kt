package br.com.xy.inc.utils

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
        if ((entityObject == null) || (entityObject.id == null))
            ResponseEntity<T>(HttpStatus.NOT_FOUND)
        else
            ResponseEntity<T>(repository.save(entityObject),  HttpStatus.OK)

    @DeleteMapping
    fun delete(@RequestBody entityObject: T) = repository.delete(entityObject)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") id: K) = repository.delete(id)

}