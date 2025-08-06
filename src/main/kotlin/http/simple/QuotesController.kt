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
    companion object {
        private val NUMBER_NAMES = listOf(
            "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
            "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty"
        )
        
        private var currentPairIndex = 0
        
        private val QUOTES = listOf(
            "The only way to do great work is to love what you do. (Steve Jobs)",
            "Life is what happens to you while you're busy making other plans. (John Lennon)",
            "The future belongs to those who believe in the beauty of their dreams. (Eleanor Roosevelt)",
            "It is during our darkest moments that we must focus to see the light. (Aristotle)",
            "The way to get started is to quit talking and begin doing. (Walt Disney)",
            "Don't let yesterday take up too much of today. (Will Rogers)",
            "You learn more from failure than from success. Don't let it stop you. Failure builds character. (Unknown)",
            "It's not whether you get knocked down, it's whether you get up. (Vince Lombardi)",
            "If you are working on something that you really care about, you don't have to be pushed. The vision pulls you. (Steve Jobs)",
            "People who are crazy enough to think they can change the world, are the ones who do. (Rob Siltanen)",
            "We don't make mistakes, just happy little accidents. (Bob Ross)",
            "In the middle of difficulty lies opportunity. (Albert Einstein)",
            "The only impossible journey is the one you never begin. (Tony Robbins)",
            "Success is not final, failure is not fatal: it is the courage to continue that counts. (Winston Churchill)",
            "The only person you are destined to become is the person you decide to be. (Ralph Waldo Emerson)",
            "Go confidently in the direction of your dreams. Live the life you have imagined. (Henry David Thoreau)",
            "When you reach the end of your rope, tie a knot in it and hang on. (Franklin D. Roosevelt)",
            "Always remember that you are absolutely unique. Just like everyone else. (Margaret Mead)",
            "Don't judge each day by the harvest you reap but by the seeds that you plant. (Robert Louis Stevenson)",
            "The future belongs to those who prepare for it today. (Malcolm X)",
            "Tell me and I forget. Teach me and I remember. Involve me and I learn. (Benjamin Franklin)",
            "The best time to plant a tree was 20 years ago. The second best time is now. (Chinese Proverb)",
            "Your limitation is only your imagination. (Unknown)",
            "Push yourself, because no one else is going to do it for you. (Unknown)",
            "Great things never come from comfort zones. (Unknown)",
            "Dream it. Wish it. Do it. (Unknown)",
            "Success doesn't just find you. You have to go out and get it. (Unknown)",
            "The harder you work for something, the greater you'll feel when you achieve it. (Unknown)",
            "Dream bigger. Do bigger. (Unknown)",
            "Don't stop when you're tired. Stop when you're done. (Unknown)",
            "Wake up with determination. Go to bed with satisfaction. (Unknown)",
            "Do something today that your future self will thank you for. (Sean Patrick Flanery)",
            "Little things make big days. (Unknown)",
            "It's going to be hard, but hard does not mean impossible. (Unknown)",
            "Don't wait for opportunity. Create it. (Unknown)",
            "Sometimes we're tested not to show our weaknesses, but to discover our strengths. (Unknown)",
            "The key to success is to focus on goals, not obstacles. (Unknown)",
            "Dream it. Believe it. Build it. (Unknown)",
            "What we think, we become. (Buddha)",
            "Be yourself; everyone else is already taken. (Oscar Wilde)",
            "Two things are infinite: the universe and human stupidity; and I'm not sure about the universe. (Albert Einstein)",
            "So many books, so little time. (Frank Zappa)",
            "A room without books is like a body without a soul. (Marcus Tullius Cicero)",
            "Be who you are and say what you feel, because those who mind don't matter, and those who matter don't mind. (Bernard M. Baruch)",
            "You know you're in love when you can't fall asleep because reality is finally better than your dreams. (Dr. Seuss)",
            "You only live once, but if you do it right, once is enough. (Mae West)",
            "Be the change that you wish to see in the world. (Mahatma Gandhi)",
            "In three words I can sum up everything I've learned about life: it goes on. (Robert Frost)",
            "If you want to know what a man's like, take a good look at how he treats his inferiors, not his equals. (J.K. Rowling)",
            "Friendship is the only cement that will ever hold the world together. (Woodrow Wilson)"
        )
        
        private fun createWordStreamWithImmediateFirst(words: List<String>, delayProvider: () -> Long): Flowable<String> {
            return if (words.isEmpty()) {
                Flowable.empty()
            } else {
                Flowable.concat(
                    Flowable.just(words.first()), // First word immediately
                    Flowable.fromIterable(words.drop(1)) // Rest of words with delay
                        .concatMap { word ->
                            val delay = delayProvider()
                            Flowable.just(word).delay(delay, TimeUnit.MILLISECONDS)
                        }
                )
            }
        }
    }

    @Get("/random", produces = [MediaType.TEXT_PLAIN])
    fun fetchRandomQuotes(): Flowable<String> {

        val quote = QUOTES[Random.nextInt(QUOTES.size)]
        println("Sending '${quote}'")

        val asWords = quote.split(" ")
        val wordsWithSpaces = asWords.mapIndexed { index, word -> if (index != asWords.size - 1) "$word " else word }

        return createWordStreamWithImmediateFirst(wordsWithSpaces) {
            // (Random.nextFloat() * 800L + 200L).toLong()
            300L // fixed delay
        }

    }

    @Get("/simple", produces = [MediaType.TEXT_PLAIN])
    fun fetchSimpleTestQuote(): Flowable<String> {

        // Get current pair of numbers
        val firstNumber = NUMBER_NAMES[currentPairIndex * 2]
        val secondNumber = NUMBER_NAMES[currentPairIndex * 2 + 1]
        val quote = "$firstNumber $secondNumber"
        
        // Increment for next time, wrapping after "nineteen twenty" (index 9)
        currentPairIndex = (currentPairIndex + 1) % 10
        
        println("Sending '${quote}'")

        val asWords = quote.split(" ")
        val wordsWithSpaces = asWords.mapIndexed { index, word -> if (index != asWords.size - 1) "$word " else word }

        return createWordStreamWithImmediateFirst(wordsWithSpaces) {
            (Random.nextFloat() * 300L + 200L).toLong()
        }
    }

    @Get("/html", produces = [MediaType.TEXT_HTML])
    fun fetchRandomQuotesHtml(): String {
        val quote = QUOTES[Random.nextInt(QUOTES.size)]
        println("Sending '${quote}'")
        return "<html><body>$quote</body></html>"
    }

}