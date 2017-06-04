package br.com.xy.inc.service

import br.com.xy.inc.service.utils.BaseResource
import br.com.xy.inc.utils.builder.ApiManager
import br.com.xy.inc.utils.builder.beans.StopResponseBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(path = arrayOf("/api/manager"))
class ApiManagerResource @Autowired constructor(val apiManager: ApiManager): BaseResource {

    @GetMapping("/log/{projectName}", consumes = arrayOf(MediaType.ALL_VALUE))
    fun getLogApi(@PathVariable("projectName") projectName: String) =
        Optional.ofNullable(apiManager.getLogStatusApi(projectName))
                .map{ r -> ResponseEntity<List<String>>(r, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))

    @GetMapping("/running", consumes = arrayOf(MediaType.ALL_VALUE))
    fun getRunningApi() =
            Optional.ofNullable(apiManager.getRunningApis())
                    .map{ r -> ResponseEntity<List<String>>(r, HttpStatus.OK) }
                    .orElse(ResponseEntity(HttpStatus.NOT_FOUND))

    @PostMapping("/start/{projectName}")
    fun startProject(@PathVariable("projectName") projectName: String) =
        if (!apiManager.startApi(projectName))
            ResponseEntity<Any>(HttpStatus.NOT_FOUND)
        else
            ResponseEntity<Any>(HttpStatus.OK)

    @PostMapping("/stop/{projectName}")
    fun stopProject(@PathVariable("projectName") projectName: String) =
        Optional.ofNullable(apiManager.stopApi(projectName))
                .map{ r -> ResponseEntity<StopResponseBean>(r, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))

    @PostMapping("/restart/{projectName}")
    fun restartProject(@PathVariable("projectName") projectName: String): ResponseEntity<Any> {
        stopProject(projectName)
        return startProject(projectName)
    }

}
