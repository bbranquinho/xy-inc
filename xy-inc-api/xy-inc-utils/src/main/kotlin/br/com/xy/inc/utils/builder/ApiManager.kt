package br.com.xy.inc.utils.builder

import br.com.xy.inc.utils.builder.beans.StopResponseBean

interface ApiManager {

    fun startApi(projectName: String): Boolean

    fun stopApi(projectName: String): StopResponseBean?

    fun getLogStatusApi(projectName: String): List<String>?

}
