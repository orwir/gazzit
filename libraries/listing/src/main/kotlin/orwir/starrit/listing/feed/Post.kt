package orwir.starrit.listing.feed

import android.content.res.Resources
import android.net.Uri
import androidx.core.net.toUri
import orwir.starrit.core.extension.toHtml
import orwir.starrit.core.util.createHashCode
import orwir.starrit.core.util.prettyDate
import orwir.starrit.core.util.squeeze
import orwir.starrit.listing.R
import orwir.starrit.listing.model.Submission
import orwir.starrit.listing.model.Subreddit
import orwir.starrit.listing.util.imageUrlOrNull
import orwir.starrit.listing.util.isImageUrl
import orwir.starrit.listing.util.videoOrNull

sealed class Post(submission: Submission, res: Resources) {
    val id: String = submission.id
    val subreddit: Subreddit = submission.subreddit
    val author: String = submission.author
    val created: String = prettyDate(submission.created * 1000, res)
    val title: String = submission.title
    val nsfw: Boolean = submission.nsfw
    val spoiler: Boolean = submission.spoiler
    val comments: String = submission.commentsCount.squeeze()
    val score: String = prettyScore(submission, res)
    val domain: String = submission.domain
    val selfDomain: Boolean = domain.startsWith("self.")
    val contentUrl = submission.url
    val postUrl = submission.permalink

    val imagePreview: String = submission.preview
        ?.images
        ?.firstOrNull()
        ?.resolutions
        ?.firstOrNull()
        ?.url
        ?: submission.thumbnail.takeIf(::isImageUrl)
        ?: ""

    val imageSource: String = submission.preview
        ?.images
        ?.firstOrNull()
        ?.source
        ?.url
        ?: submission.imageUrlOrNull()
        ?: ""

    override fun equals(other: Any?) =
        other is Post
                && other::class == this::class
                && id == other.id
                && subreddit == other.subreddit
                && author == other.author
                && created == other.created
                && title == other.title
                && nsfw == other.nsfw
                && spoiler == other.spoiler
                && comments == other.comments
                && score == other.score
                && domain == other.domain
                && contentUrl == other.contentUrl
                && postUrl == other.postUrl

    override fun hashCode() = createHashCode(
        id,
        subreddit,
        author,
        created,
        title,
        nsfw,
        spoiler,
        comments,
        score,
        domain,
        contentUrl,
        postUrl
    )

    private fun prettyScore(submission: Submission, res: Resources) =
        if (submission.hideScore) {
            res.getString(R.string.score_hidden)
        } else {
            submission.score.squeeze()
        }
}

class LinkPost(
    submission: Submission,
    resources: Resources
) : Post(submission, resources) {
    val link: Uri = contentUrl.toUri()
    val displayLink: String = link.authority ?: submission.url
}

class ImagePost(
    submission: Submission,
    resources: Resources
) : Post(submission, resources) {
    private val image = submission.preview
        ?.images
        ?.firstOrNull()
        ?.resolutions
        ?.firstOrNull()
    val imageWidth: Int = image?.width ?: 0
    val imageHeight: Int = image?.height ?: 0
}

class GifPost(
    submission: Submission,
    resources: Resources
) : Post(submission, resources) {
    val gif: String = submission.url
}

class TextPost(
    submission: Submission,
    resources: Resources
) : Post(submission, resources) {
    val text: CharSequence = (submission.selftextHtml ?: submission.selftext ?: "").toHtml()
}

class VideoPost(
    submission: Submission,
    resources: Resources
) : Post(submission, resources) {
    val video: String = submission.videoOrNull() ?: ""
}