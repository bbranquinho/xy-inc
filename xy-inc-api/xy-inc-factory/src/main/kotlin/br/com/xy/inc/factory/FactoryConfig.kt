package br.com.xy.inc.factory

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = ["br.com.xy.inc"])
open class FactoryConfig {

    @Bean
    open fun newsApi() =
            Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .paths(regex("/api/.*"))
                    .build()

    private fun apiInfo() =
            ApiInfoBuilder()
                    .title("XY-Inc")
                    .description("Swagger API")
                    .version("0.0.1-SNAPSHOT")
                    .build()

}
