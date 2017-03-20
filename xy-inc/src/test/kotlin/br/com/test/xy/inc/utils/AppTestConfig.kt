package br.com.test.xy.inc.utils

import br.com.xy.inc.utils.AppConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = AppConfig::class)
open class AppTestConfig {

}
