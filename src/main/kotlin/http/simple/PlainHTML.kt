package http.simple

import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/")
class PlainHTML {
    @SingleResult
    @Get("hi/{name}", produces = [MediaType.TEXT_HTML])
    fun genHello(name: String): String {
        val html = "<html><body>${name}! Hi! </body></html>"
        println("returning: $html")
        return html
    }

}