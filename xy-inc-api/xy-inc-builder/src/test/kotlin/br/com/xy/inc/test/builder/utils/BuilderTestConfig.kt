package br.com.xy.inc.test.builder.utils

import br.com.xy.inc.builder.utils.BuilderConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import(BuilderConfig::class)
@PropertySource("classpath:application-builder-test.properties")
@ComponentScan(basePackages = arrayOf("br.com.xy.inc.test.builder"))
open class BuilderTestConfig {

}
