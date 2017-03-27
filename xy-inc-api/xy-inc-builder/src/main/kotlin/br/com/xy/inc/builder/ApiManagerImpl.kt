package br.com.xy.inc.builder

import br.com.xy.inc.utils.ApplicationProperties
import br.com.xy.inc.utils.builder.ApiBuilder
import br.com.xy.inc.utils.builder.ApiManager
import br.com.xy.inc.utils.builder.beans.StopResponseBean
import br.com.xy.inc.utils.template.ProjectBean
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue


@Component
open class ApiManagerImpl: ApiManager {

    val logger = LoggerFactory.getLogger(ApiManagerImpl::class.java)

    @Autowired
    lateinit var apiBuilder: ApiBuilder

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var applicationProperties: ApplicationProperties

    private val apiCache = ConcurrentHashMap<String, ApiRunnable>()

    override fun startApi(projectName: String): Boolean {
        var project = apiBuilder.getProject(projectName)

        if (project == null) {
            logger.warn("Project [%s] not found.".format(projectName))
            return false
        }

        if (apiCache.get(projectName) == null) {
            val apiRunnable = ApiRunnable(project, applicationProperties.apiLogSize, { apiCache.remove(project.name) })

            apiCache.put(project.name, apiRunnable);

            apiRunnable.thread = Thread(apiRunnable)

            apiRunnable.start()

            return true
        } else {
            return false
        }
    }

    override fun stopApi(projectName: String): StopResponseBean? {
        var project = apiBuilder.getProject(projectName)

        if (project == null) {
            logger.warn("Project [%s] not found.".format(projectName))
            return null
        }

        var shutdownResponse: StopResponseBean? = null

        try {
            shutdownResponse = restTemplate.postForEntity("http://localhost:" + project.port + "/" + project.name + "/shutdown", {}, StopResponseBean::class.java).body
        } catch (e: Exception) {
            logger.debug(e.message)
        }

        apiCache.remove(projectName)?.stop()

        return shutdownResponse
    }

    override fun getLogStatusApi(projectName: String): List<String>? {
        val apiRunnable = apiCache.get(projectName)

        if (apiRunnable != null) {
            return apiRunnable.getLog()
        }

        return null
    }

    private class ApiRunnable(val project: ProjectBean, var maxQueueSize: Int?, val onFinish: () -> ApiRunnable?): Runnable {

        val logger = LoggerFactory.getLogger(ApiRunnable::class.java)

        val logQueue = ConcurrentLinkedQueue<String>()

        var thread: Thread? = null

        override fun run() {
            val projectDir = System.getProperty("user.dir")
            var script: String
            val apiDir = projectDir + "/projects/" + project.name
            val warDir = "build/libs/" + project.name + "-" + project.version + ".war"

            when(System.getProperty("os.name").toLowerCase()) {
                "linux", "mac os x" -> { script = projectDir + "/start_api.sh" }
                "win" -> { script = projectDir + "/start_api.bat" }
                else -> throw Exception("OS [%s] not supported yet.".format(System.getProperty("os.name")))
            }

            try {
                var process = Runtime.getRuntime().exec(arrayOf(script, apiDir, warDir))

                logger.info("API: [%s] started".format(project.name))

                val input = BufferedReader(InputStreamReader(process.getInputStream()))

                var line = input.readLine()

                while (line != null) {
                    logQueue.add(line)

                    logger.debug("API: [%s] output [%s]".format(project.name, line))

                    while ((maxQueueSize != null) && (logQueue.size > maxQueueSize!!)) {
                        logQueue.poll()
                    }

                    line = input.readLine()
                }
                input.close()

                if (process.waitFor() != 0) {
                    throw Exception("Error to start the API [%s][%s].".format(project.name, formatError(process)))
                }
            } catch (e: Exception) {
                logger.error(e.message, e)
            }

            logger.info("API: [%s] stopped".format(project.name))

            onFinish()
        }

        fun getLog() = logQueue.toList()
        
        // Stop a thread not is a good practice, but in this case we have to do it.
        @Suppress("DEPRECATION")
        fun stop() = thread?.stop()

        fun start() = thread?.start()

        fun formatError(proc: Process): String {
            val input = BufferedReader(InputStreamReader(proc.getErrorStream()))
            var line = input.readLine()
            var output = ""

            while (line != null) {
                output += line
                line = input.readLine()
            }

            input.close()

            return output
        }

    }

}