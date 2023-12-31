package http.simple

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

@Controller("/alphabet")
class AlphabetController {
    @Get("{length}", produces = [MediaType.TEXT_PLAIN])
    fun genLetters(length: Int): String {
        return ('A'..'Z').asSequence().cycle().take(length).joinToString("")
    }
}

@Controller("/cab")
class ChunkedAlphabetController {
    @Get(value = "{length}", produces = [MediaType.TEXT_PLAIN])
    fun genLetters(length: Int): Flowable<String> {
        return Flowable.fromIterable(('A'..'Z').asSequence().cycle().take(length).toList())
            .buffer(10)
            .zipWith(Flowable.interval(1, TimeUnit.SECONDS)) { item, _ -> item.joinToString("") }
    }
}

fun <T> Sequence<T>.cycle() = sequence { while (true) yieldAll(this@cycle) }
