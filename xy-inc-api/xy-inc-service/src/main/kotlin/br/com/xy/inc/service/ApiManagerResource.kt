package br.com.xy.inc.service

import br.com.xy.inc.utils.builder.ApiManager
import br.com.xy.inc.utils.builder.beans.StopResponseBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(path = arrayOf("/api/manager"), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
open class ApiManagerResource {

    @Autowired
    lateinit var apiManager: ApiManager

    @GetMapping("/log/{projectName}", consumes = arrayOf(MediaType.ALL_VALUE))
    fun getLogApi(@PathVariable("projectName") projectName: String): ResponseEntity<List<String>> {
        return Optional.ofNullable(apiManager.getLogStatusApi(projectName))
                .map{ r -> ResponseEntity<List<String>>(r, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @PostMapping("/start/{projectName}")
    fun startProject(@PathVariable("projectName") projectName: String): ResponseEntity<Any> {
        if (!apiManager.startApi(projectName)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            return ResponseEntity(HttpStatus.OK)
        }
    }

    @PostMapping("/restart/{projectName}")
    fun restartProject(@PathVariable("projectName") projectName: String): ResponseEntity<Any> {
        stopProject(projectName)

        return startProject(projectName)
    }

    @PostMapping("/stop/{projectName}")
    fun stopProject(@PathVariable("projectName") projectName: String): ResponseEntity<StopResponseBean>? {
        return Optional.ofNullable(apiManager.stopApi(projectName))
                .map{ r -> ResponseEntity<StopResponseBean>(r, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

}
