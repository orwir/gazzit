package orwir.gazzit.listing.internal

import org.koin.core.KoinComponent
import org.koin.core.inject
import orwir.gazzit.listing.ListingRepository
import orwir.gazzit.listing.feed.Feed
import orwir.gazzit.listing.feed.FeedType
import orwir.gazzit.listing.feed.PostResolver

internal class BaseListingRepository : ListingRepository, KoinComponent {

    private val service: ListingService by inject()
    private val resolver: PostResolver by inject()

    override suspend fun feed(
        type: FeedType,
        before: String?,
        after: String?,
        limit: Int?
    ): Feed =
        when (type) {
            is FeedType.Subreddit -> service.subreddit(
                subreddit = type.name,
                before = before,
                after = after,
                limit = limit
            )
            is FeedType.Best -> service.best(
                before = before,
                after = after,
                limit = limit
            )
        }.run {
            Feed(data.children.map { resolver.resolve(it.data) }, before, after)
        }

}