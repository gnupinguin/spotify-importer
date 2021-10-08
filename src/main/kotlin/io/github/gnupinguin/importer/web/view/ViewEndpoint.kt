package io.github.gnupinguin.importer.web.view

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping
class ViewEndpoint {

    @GetMapping(value = [""], produces = [MediaType.TEXT_HTML_VALUE])
    fun index(): String {
        return "index.html"
    }

    @GetMapping(value = ["import"], produces = [MediaType.TEXT_HTML_VALUE])
    fun result(): String {
        return "import.html"
    }

}