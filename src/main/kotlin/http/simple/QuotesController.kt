package http.simple

import data.Quotation
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.reactivex.Flowable
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
//        if (bufferedQuotes.isEmpty()) {
//            bufferedQuotes.addAll(quotableClient.fetchRandomQuotes())
//        }
//        val quote = bufferedQuotes.removeFirst()
//        val sentence = String.format("%s (%s)", quote.content, quote.author).trim()
//        println("Sending '${sentence}'")
//        val asWords = sentence.split(" ")
//        val wordsWithSpaces = asWords.mapIndexed { index, word -> if (index != asWords.size - 1) "$word " else word }
//
//        return Flowable.fromIterable(wordsWithSpaces)
//            .concatMap { word ->
//                val delay = Random.nextFloat() * 50L // 0-50ms delay between words
//                Flowable.just(word).delay(delay.toLong(), TimeUnit.MILLISECONDS)
//            }

        val quotes = listOf(
            "This is quote 1 (a test)",
            "Another great quote here that has a lot more words in it (anon 2)",
            "wow all these quotes and non of them have lorum ipsum in them, oh, wait. (anon 3)",
            "another random quote (here)",
            "what I wouldn't give for more quotes (there)",
            "to be or not to be is missing here (other)",
            "something something something (fenrock)",
            "something about technology being not working (misquoted adams)"
        )
        val quote = quotes[Random.nextInt(quotes.size)]
        println("Sending '${quote}'")

        val asWords = quote.split(" ")
        val wordsWithSpaces = asWords.mapIndexed { index, word -> if (index != asWords.size - 1) "$word " else word }

        return Flowable.fromIterable(wordsWithSpaces)
            .concatMap { word ->
                val delay = Random.nextFloat() * 50L // 0-50ms delay between words
                Flowable.just(word).delay(delay.toLong(), TimeUnit.MILLISECONDS)
            }

    }

    @Get("/html", produces = [MediaType.TEXT_HTML])
    fun fetchRandomQuotesHtml(): String {
        if (bufferedQuotes.isEmpty()) {
            bufferedQuotes.addAll(quotableClient.fetchRandomQuotes())
        }
        val quote = bufferedQuotes.removeFirst()
        val sentence = String.format("%s (%s)", quote.content, quote.author).trim()
        println("Sending '${sentence}")
        return "<html><body>$sentence </body></html>"
    }

}