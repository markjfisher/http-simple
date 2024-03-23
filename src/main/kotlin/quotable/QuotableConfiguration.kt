package quotable

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires

@ConfigurationProperties(QuotableConfiguration.PREFIX)
@Requires(property = QuotableConfiguration.PREFIX)
class QuotableConfiguration {
    var limit: Int = 10
    var maxLength: Int = 90

    companion object {
        const val PREFIX = "quotable"
    }
}