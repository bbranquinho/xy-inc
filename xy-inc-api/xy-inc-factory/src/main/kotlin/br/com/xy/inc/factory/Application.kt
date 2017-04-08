package br.com.xy.inc.factory

import br.com.xy.inc.factory.utils.FactoryConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
open class Application : SpringBootServletInitializer() {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(FactoryConfig::class.java, *args)
        }
    }

    override fun configure(application: SpringApplicationBuilder) =
            application.sources(Application::class.java)

}

