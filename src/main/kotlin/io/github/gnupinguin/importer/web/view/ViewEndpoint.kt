package io.github.gnupinguin.importer.web.view

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ViewEndpoint {

    @GetMapping(value = [""], produces = [MediaType.TEXT_HTML_VALUE])
    fun index(): String {
        return "static/html/index.html"
    }

    @GetMapping(value = [""], produces = [MediaType.TEXT_HTML_VALUE])
    fun result(): String {
        return "static/html/result.html"
    }

}