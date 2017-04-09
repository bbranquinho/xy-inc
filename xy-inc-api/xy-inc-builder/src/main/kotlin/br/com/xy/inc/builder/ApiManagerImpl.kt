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
            logger.warn("Project [{}] not found.", projectName)
            return false;
        }

        apiCache.computeIfAbsent(projectName, {
            logger.debug("Starting the API [{}]", it)

            val apiRunnable = ApiRunnable(project, applicationProperties.apiLogSize, { apiCache.remove(it) })

            apiRunnable.thread = Thread(apiRunnable)
            apiRunnable.start()
            apiRunnable
        })

        return true
    }

    override fun stopApi(projectName: String): StopResponseBean? {
        logger.debug("Stopping the API [{}]", projectName)

        var project = apiBuilder.getProject(projectName)

        if (project == null) {
            logger.warn("Project [{}] not found.", projectName)
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

    override fun getLogStatusApi(projectName: String) =
        apiCache.get(projectName)?.getLog()

    private class ApiRunnable(val project: ProjectBean, val maxQueueSize: Int?, val onFinish: () -> Unit): Runnable {

        val logger = LoggerFactory.getLogger(ApiRunnable::class.java)

        val logQueue = ConcurrentLinkedQueue<String>()

        var thread: Thread? = null

        override fun run() {
            val projectDir = System.getProperty("user.dir")
            val apiDir = projectDir + "/projects/" + project.name
            val warDir = "build/libs/" + project.name + "-" + project.version + ".war"
            val os = System.getProperty("os.name").toLowerCase()

            var script = if (os.startsWith("linux") || os.startsWith("mac"))
                projectDir + "/start_api.sh"
            else if (os.startsWith("win"))
                projectDir + "/start_api.bat"
            else
                throw Exception("OS [%s] not supported yet.".format(os))

            try {
                var process = Runtime.getRuntime().exec(arrayOf(script, apiDir, warDir))
                val input = BufferedReader(InputStreamReader(process.getInputStream()))
                var line = input.readLine()

                logger.info("API: [{}] started", project.name)

                while (line != null) {
                    logQueue.add(line)
                    logger.debug("API: [{}] output [{}]", project.name, line)

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

            logger.info("API: [{}] stopped", project.name)

            onFinish()
        }

        fun getLog() = logQueue.toList()

        // Stop a thread not is a good practice, but in this case we can and have to do it.
        @Suppress("DEPRECATION")
        fun stop() = thread?.stop()

        fun start() = thread?.start()

        private fun formatError(proc: Process): String {
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