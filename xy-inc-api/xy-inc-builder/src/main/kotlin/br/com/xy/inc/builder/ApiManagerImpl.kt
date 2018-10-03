package br.com.xy.inc.builder

import br.com.xy.inc.utils.ApplicationProperties
import br.com.xy.inc.utils.builder.ApiBuilder
import br.com.xy.inc.utils.builder.ApiManager
import br.com.xy.inc.utils.builder.beans.StopResponseBean
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.concurrent.ConcurrentHashMap

@Component
class ApiManagerImpl @Autowired constructor(val apiBuilder: ApiBuilder, val restTemplate: RestTemplate,
                                            val applicationProperties: ApplicationProperties) : ApiManager {

    private val logger = LoggerFactory.getLogger(ApiManagerImpl::class.java)

    private val apiCache = ConcurrentHashMap<String, ApiRunnable>()

    override fun startApi(projectName: String): Boolean {
        var project = apiBuilder.getProject(projectName)

        if (project == null) {
            logger.warn("Project [{}] not found.", projectName)
            return false
        }

        apiCache.computeIfAbsent(projectName) {
            ApiRunnable(project, applicationProperties.apiLogSize) {
                apiCache.remove(it)
            }.start()
        }

        return true
    }

    override fun stopApi(projectName: String): StopResponseBean? {
        var project = apiBuilder.getProject(projectName)

        if (project == null) {
            logger.warn("Project [{}] not found.", projectName)
            return null
        }

        logger.debug("Stopping the API [{}].", projectName)

        try {
            restTemplate.postForLocation("http://localhost:${project.port}/${project.name}/shutdown", {})
        } catch (e: Exception) {
            logger.debug(e.message)
        }

        apiCache.remove(projectName)?.stop()

        return StopResponseBean("$projectName stopped.")
    }

    override fun getLogStatusApi(projectName: String) =
            apiCache[projectName]?.getLog()

    override fun getRunningApis() =
            apiCache.keys().toList()

}