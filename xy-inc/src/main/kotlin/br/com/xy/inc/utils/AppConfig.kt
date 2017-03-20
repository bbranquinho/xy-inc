package br.com.xy.inc.utils

import br.com.xy.inc.Application
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = arrayOf(Application::class))
open class AppConfig {

    @Bean
    open fun newsApi() = Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().paths(regex("/api/.*")).build()

    fun apiInfo() = ApiInfoBuilder().title("XY-Inc").description("Swagger API").version("0.0.1-SNAPSHOT").build()

}