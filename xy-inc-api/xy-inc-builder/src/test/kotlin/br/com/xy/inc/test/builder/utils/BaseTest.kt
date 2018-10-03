package br.com.xy.inc.test.builder.utils

import org.junit.runner.RunWith
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [ConfigFileApplicationContextInitializer::class])
@ContextConfiguration(classes = [BuilderTestConfig::class])
abstract class BaseTest
