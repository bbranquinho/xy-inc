package br.com.xy.inc.builder.utils

import br.com.xy.inc.utils.UtilsConfig
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(UtilsConfig::class)
@ComponentScan(basePackages = arrayOf("br.com.xy.inc.builder"))
open class BuilderConfig {

    @Bean
    open fun getMapper(): ObjectMapper {
        return ObjectMapper()
    }

}
