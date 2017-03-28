package br.com.xy.inc.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
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

    @GetMapping("/{id:[0-9.,]*$}")
    fun findById(@PathVariable(value = "id") id: K) =
        Optional.ofNullable(repository.findOne(id))
                .map{ r -> ResponseEntity<T>(r, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))

    @GetMapping(consumes = arrayOf(MediaType.ALL_VALUE))
    fun findAll(@RequestParam("page", required = false) page: Int?, @RequestParam("size", required = false) size: Int?,
                @RequestParam("fields", required = false) fields: String?, @RequestParam("direction", required = false) direction: String?)
            : ResponseEntity<List<T>> {
        if ((size == null) || (page == null) || (page < 0) || (size <= 0)) {
            return ResponseEntity(repository.findAll(), HttpStatus.OK)
        }

        val searchDirection = Direction.fromStringOrNull(direction)
        val searchFields = fields?.trim()?.split(",")?.filter { !it.isEmpty() }

        var pageRequest: PageRequest

        if ((searchDirection == null) || (searchFields == null) || searchFields.isEmpty()) {
            pageRequest = PageRequest(page, size)
        } else {
            pageRequest = PageRequest(page, size, Sort(searchDirection, searchFields))
        }

        val pageResult = repository.findAll(pageRequest)

        val headers = PaginationUtil.generatePaginationHttpHeaders(pageResult, getBaseUrl())

        return ResponseEntity(pageResult.content, headers, HttpStatus.OK)
    }

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

    @DeleteMapping("/{id:[0-9.,]*$}")
    fun delete(@PathVariable(value = "id") id: K) = repository.delete(id)

    abstract fun getBaseUrl(): String

    protected fun mountPage(page: Int?, size: Int?, sort: Sort): PageRequest {
        return PageRequest(page!!, size!!, sort)
    }

    protected fun mountSort(direction: Direction?, fields: List<String>?): Sort? {
        if (fields == null || fields.size <= 0) {
            return null
        }

        if (direction == null) {
            return Sort(Direction.ASC, fields)
        }

        return Sort(direction, fields)
    }

}
