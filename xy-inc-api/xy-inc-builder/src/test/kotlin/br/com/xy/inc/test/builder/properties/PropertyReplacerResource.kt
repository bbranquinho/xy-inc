package br.com.xy.inc.test.builder.properties

import br.com.xy.inc.builder.properties.PairProperty
import br.com.xy.inc.builder.utils.PropertyReplacer
import br.com.xy.inc.test.builder.utils.BaseTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

open class PropertyReplacerResource: BaseTest() {

    @Autowired
    lateinit var propertyReplacer: PropertyReplacer

    @Test
    fun testPropertyReplacerResourceApplication() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("projectName", "xy-inc"))
        properties.add(PairProperty("projectPort", "8081"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/resources/application.xyi")

        assertEquals(fileText,
"""spring:
  application:
    name: xy-inc
  profiles:
    active: dev

server:
  contextPath: /xy-inc
  port: 8081
""")
    }

    @Test
    fun testPropertyReplacerResourceApplicationDev() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("projectName", "xy-inc"))
        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("databaseName", "xy_inc"))
        properties.add(PairProperty("databaseUsername", "root"))
        properties.add(PairProperty("databasePassword", "root"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/resources/application-dev.xyi")

        assertEquals(fileText,
"""spring:
  jackson:
    serialization:
      indent_output: true
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:h2:mem:xy_inc
    username: root
    password: root
  jpa:
    database: H2
    show-sql: true
    hibernate:
      ddl-auto: create
  h2:
    console:
      enabled: true
logging:
  file: build/xy-inc.log
  level:
    br.com.xy.inc: DEBUG
    org.springframework: INFO
    org.hibernate: INFO
endpoints:
  shutdown:
    enabled: true
""")
    }

    @Test
    fun testPropertyReplacerResourceApplicationProd() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("projectName", "xy-inc"))
        properties.add(PairProperty("packageName", "br.com.xy.inc"))
        properties.add(PairProperty("databaseName", "xy_inc"))
        properties.add(PairProperty("databaseUsername", "root"))
        properties.add(PairProperty("databasePassword", "root"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/resources/application-prod.xyi")

        assertEquals(fileText,
"""spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:mysql://localhost:3306/xy_inc?autoReconnect=true&useSSL=false
    username: root
    password: root
    hikari:
      data-source-properties:
        cachePrepStmts: true
        prepStmtCacheSize: 250
        prepStmtCacheSqlLimit: 2048
        useServerPrepStmts: true
  jpa:
    database: mysql
    show-sql: false
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL5Dialect
  h2:
    console:
      enabled: false
logging:
  file: build/xy-inc.log
  level:
    br.com.xy.inc: INFO
    org.springframework: WARN
    org.hibernate: WARN
""")
    }

    @Test
    fun testPropertyReplacerResourceApplicationJBossWeb() {
        val properties = ArrayList<PairProperty>()

        properties.add(PairProperty("projectName", "xy-inc"))

        val fileText = propertyReplacer.replaceFieldProperties(properties, "/templates/resources/jboss-web.xyi")

        assertEquals(fileText,
"""<?xml version="1.0" encoding="UTF-8"?>
<jboss-web>
    <context-root>xy-inc</context-root>
</jboss-web>
""")
    }

}