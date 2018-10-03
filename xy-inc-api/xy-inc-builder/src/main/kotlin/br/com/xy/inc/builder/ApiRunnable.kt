package br.com.xy.inc.builder

import br.com.xy.inc.utils.template.ProjectBean
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentLinkedQueue

class ApiRunnable(val project: ProjectBean, val maxQueueSize: Int?, val onFinish: () -> Unit): Runnable {

    private val logger = LoggerFactory.getLogger(ApiRunnable::class.java)

    private val logQueue = ConcurrentLinkedQueue<String>()

    private val thread: Thread = Thread(this)

    override fun run() {
        logger.debug("Starting the API [{}]", project.name)

        val projectDir = System.getProperty("user.dir")
        val apiDir = "${projectDir}/projects/${project.name}"
        val warDir = "build/libs/${project.name}-${project.version}.war"
        val os = System.getProperty("os.name").toLowerCase()

        var script = if (os.startsWith("linux") || os.startsWith("mac"))
            "$projectDir/start_api.sh"
        else if (os.startsWith("win"))
            "$projectDir/start_api.bat"
        else
            throw Exception("OS [${os}] not supported yet.")

        try {
            var process = Runtime.getRuntime().exec(arrayOf(script, apiDir, warDir))
            val input = BufferedReader(InputStreamReader(process.inputStream))
            var line = input.readLine()

            logger.info("API: [{}] started.", project.name)

            while (line != null) {
                logQueue.add(line)
                logger.debug("API: [{}] output [{}].", project.name, line)

                while ((maxQueueSize != null) && (logQueue.size > maxQueueSize)) {
                    logQueue.poll()
                }

                line = input.readLine()
            }
            input.close()

            if (process.waitFor() != 0) {
                throw Exception("Error to start the API [${project.name}][${formatError(process)}].")
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
        }

        logger.info("API: [{}] stopped.", project.name)

        onFinish()
    }

    fun getLog() = logQueue.toList()

    // Stop a thread not is a good practice, but in this case we can and have to do it.
    @Suppress("DEPRECATION")
    fun stop() =
            thread.stop()

    fun start(): ApiRunnable {
        thread.start()
        return this
    }

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