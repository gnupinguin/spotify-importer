package io.github.gnupinguin.spotify.web.view

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
class ViewEndpoint {

    @GetMapping(value = [""], produces = [MediaType.TEXT_HTML_VALUE])
    fun index(): String {
        return "index.html"
    }

    @GetMapping(value = ["import"], produces = [MediaType.TEXT_HTML_VALUE])
    fun importSongs(): String {
        return "import.html"
    }

    @GetMapping(value = ["saved-artists"], produces = [MediaType.TEXT_HTML_VALUE])
    fun playlist(): String {
        return "playlist.html"
    }

}