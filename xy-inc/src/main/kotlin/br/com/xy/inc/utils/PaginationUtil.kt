package br.com.xy.inc.utils

import org.springframework.data.domain.Page
import org.springframework.http.HttpHeaders
import org.springframework.web.util.UriComponentsBuilder

import java.net.URISyntaxException

object PaginationUtil {

    @Throws(URISyntaxException::class)
    fun generatePaginationHttpHeaders(page: Page<*>, baseUrl: String): HttpHeaders {
        val headers = HttpHeaders()

        headers.add("X-Total-Count", "" + page.totalElements)

        var link = ""
        if (page.number + 1 < page.totalPages) {
            link = "<" + generateUri(baseUrl, page.number + 1, page.size) + ">; rel=\"next\","
        }

        if (page.number > 0) {
            link += "<" + generateUri(baseUrl, page.number - 1, page.size) + ">; rel=\"prev\","
        }

        var lastPage = 0
        if (page.totalPages > 0) {
            lastPage = page.totalPages - 1
        }
        link += "<" + generateUri(baseUrl, lastPage, page.size) + ">; rel=\"last\","
        link += "<" + generateUri(baseUrl, 0, page.size) + ">; rel=\"first\""

        headers.add(HttpHeaders.LINK, link)

        return headers
    }

    @Throws(URISyntaxException::class)
    private fun generateUri(baseUrl: String, page: Int, size: Int): String {
        return UriComponentsBuilder.fromUriString(baseUrl).queryParam("page", page).queryParam("size", size).toUriString()
    }

}

