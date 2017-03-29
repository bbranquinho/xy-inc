package br.com.xy.inc.service.utils

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping(produces = arrayOf(MediaType.APPLICATION_JSON_VALUE), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
interface BaseResource {

}
