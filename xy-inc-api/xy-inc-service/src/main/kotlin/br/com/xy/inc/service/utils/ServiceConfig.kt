package br.com.xy.inc.service.utils

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = arrayOf("br.com.xy.inc.service"))
open class ServiceConfig
