package http.simple

import data.Quotation
import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit
import quotable.QuotableClient

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/quotes")
class QuotesController(
    private val quotableClient: QuotableClient
) {
    private val bufferedQuotes: MutableList<Quotation> = mutableListOf()

    @Get("/random", produces = [MediaType.TEXT_PLAIN])
    // @SingleResult
    fun fetchRandomQuotes(): Flowable<String> {
        if (bufferedQuotes.isEmpty()) {
            // get some more quotes into the buffer
            bufferedQuotes.addAll(quotableClient.fetchRandomQuotes())
        }
        val quote = bufferedQuotes.removeFirst()
        val sentence = String.format("%s (%s)", quote.content, quote.author).trim()
        val asWords = sentence.split(" ")
        val wordsWithSpaces = asWords.mapIndexed { index, word -> if (index != asWords.size - 1) "$word " else word }

        return Flowable.fromIterable(wordsWithSpaces)
            .zipWith(Flowable.interval(200, TimeUnit.MILLISECONDS)) { word, _ -> word }
    }
}