package http.simple

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

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
        println("=== ENDPOINT CALLED with length: $length ===")
        logger.info {"Starting chunked alphabet generation for length: ${length}"}
        
        return Flowable.fromIterable(('A'..'Z').asSequence().cycle().take(length).toList())
            .buffer(10)
            .doOnNext { chunk -> 
                println("Buffered chunk of size: ${chunk.size}, first char: ${chunk.firstOrNull()}" )
                logger.info {"Buffered chunk of size: ${chunk.size}, first char: ${chunk.firstOrNull()}" }
            }
            .zipWith(
                Flowable.interval(2000, 500, TimeUnit.MILLISECONDS)
                    .doOnNext { tick -> 
                        println("Interval tick: $tick at ${System.currentTimeMillis()}")
                        logger.info {"Interval tick: ${tick}"}
                    }
            ) { item, tick -> 
                val result = item.joinToString("")
                val preview = result.take(10) + if (result.length > 10) "..." else ""
                println("Emitting chunk $tick with content: $preview")
                logger.info{"Emitting chunk ${tick} with content: ${preview}"}
                result
            }
            .doOnComplete { 
                println("=== COMPLETED ===")
                logger.info{"Completed chunked alphabet generation"}
            }
            .doOnError { error -> 
                println("=== ERROR: $error ===")
                logger.error(error) {"Error in chunked alphabet generation"}
            }
    }
}

fun <T> Sequence<T>.cycle() = sequence { while (true) yieldAll(this@cycle) }
