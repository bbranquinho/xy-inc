package br.com.xy.inc.factory.utils

import br.com.xy.inc.builder.utils.BuilderConfig
import br.com.xy.inc.service.utils.ServiceConfig
import br.com.xy.inc.utils.UtilsConfig
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@EnableAutoConfiguration
@Configuration
@EnableSwagger2
@Import(BuilderConfig::class, ServiceConfig::class, UtilsConfig::class)
@ComponentScan(basePackageClasses = arrayOf(FactoryConfig::class))
open class FactoryConfig {

    @Bean
    open fun newsApi() = Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().paths(regex("/api/.*")).build()

    private fun apiInfo() = ApiInfoBuilder().title("XY-Inc").description("Swagger API").version("0.0.1-SNAPSHOT").build()

}
