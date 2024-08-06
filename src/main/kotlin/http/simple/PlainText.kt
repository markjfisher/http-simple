package http.simple

import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/")
class PlainText {
    @SingleResult
    @Get("text/{data}", produces = [MediaType.TEXT_PLAIN])
    fun echo(data: String): String {
        return data
    }

}