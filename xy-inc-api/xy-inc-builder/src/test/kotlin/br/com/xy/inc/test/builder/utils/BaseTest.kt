package br.com.xy.inc.test.builder.utils

import org.junit.Assert
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(ConfigFileApplicationContextInitializer::class))
@ContextConfiguration(classes = arrayOf(BuilderTestConfig::class))
abstract class BaseTest {

    fun logAndFail(message: String, logger: Logger) {
        logger.error(message)

		Assert.fail(message)
	}

	fun logAndFail(message: String, e: Exception, logger: Logger) {
        logger.error(message, e)

        Assert.fail(message)
	}

}
