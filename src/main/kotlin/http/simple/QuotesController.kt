package http.simple

import data.Quotation
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import quotable.QuotableClient

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/quotes")
class QuotesController(
    private val quotableClient: QuotableClient
) {
    private val bufferedQuotes: MutableList<Quotation> = mutableListOf()

    @Get("/random", produces = [MediaType.TEXT_PLAIN])
    fun fetchRandomQuotes(): Flowable<String> {
        if (bufferedQuotes.isEmpty()) {
            bufferedQuotes.addAll(quotableClient.fetchRandomQuotes())
        }
        val quote = bufferedQuotes.removeFirst()
        val sentence = String.format("%s (%s)", quote.content, quote.author).trim()
        println("Sending '${sentence}")
        val asWords = sentence.split(" ")
        val wordsWithSpaces = asWords.mapIndexed { index, word -> if (index != asWords.size - 1) "$word " else word }

        return Flowable.fromIterable(wordsWithSpaces)
            .concatMap { word ->
                val delay = Random.nextFloat() * 50L // 0-50ms delay between words
                Flowable.just(word).delay(delay.toLong(), TimeUnit.MILLISECONDS)
            }
    }

}