package quotable

import data.Quotation
import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.HttpHeaders.ACCEPT
import io.micronaut.http.HttpHeaders.USER_AGENT
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Headers
import io.micronaut.http.client.annotation.Client

@Client(id = "quotable")
@Headers(
    Header(name = USER_AGENT, value = "Micronaut HTTP Client"),
    Header(name = ACCEPT, value = "application/json")
)
interface QuotableClient {
    @Get("/quotes/random?limit=\${quotable.limit}&maxLength=\${quotable.maxLength}")
    @SingleResult
    fun fetchRandomQuotes(): List<Quotation>
}