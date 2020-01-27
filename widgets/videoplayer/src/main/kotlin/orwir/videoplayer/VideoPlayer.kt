package orwir.videoplayer

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.TimeBar
import kotlinx.android.synthetic.main.videoplayer.view.*
import kotlinx.android.synthetic.main.vp_controller.view.*
import kotlinx.coroutines.*

class VideoPlayer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr),
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    val cover: ImageView by lazy { vp_cover }
    internal lateinit var player: ExoPlayer
    internal lateinit var video: MediaSource

    private var initial: Boolean = true
    private var started: Boolean = false
    private var repeat: Boolean = true
    private var volume: Float = 0f
    private var progressJob: Job? = null

    private val listener = createListener()
    private val hud: View by lazy { vp_controller }
    private val repeatOn: Drawable
    private val repeatOff: Drawable
    private val volumeOn: Drawable
    private val volumeOff: Drawable
    private val fullscreenOn: Drawable
    private val fullscreenOff: Drawable

    init {
        LayoutInflater.from(context).inflate(R.layout.videoplayer, this, true)
        repeatOn = ContextCompat.getDrawable(context, R.drawable.ic_repeat_on)!!
        repeatOff = ContextCompat.getDrawable(context, R.drawable.ic_repeat_off)!!
        volumeOn = ContextCompat.getDrawable(context, R.drawable.ic_volume_on)!!
        volumeOff = ContextCompat.getDrawable(context, R.drawable.ic_volume_off)!!
        fullscreenOn = ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_on)!!
        fullscreenOff = ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_off)!!
        initControllerListeners()
        vp_timebar.addListener(listener)
    }

    fun setVideo(uri: Uri) {
        video = uri.toMediaSource(context)
    }

    fun start(restored: Boolean = false) {
        VideoPlayerHolder.swap(this)
        player.prepare(video, !restored, initial)
        player.addListener(listener)
        setVolume(volume)
        setRepeat(repeat)
        if (!restored) player.playWhenReady = true
        vp_surface.player = player
        vp_surface.setVisible(true)
        showHUD(show = !player.playWhenReady, state = player.playWhenReady)
        if (progressJob == null || progressJob?.isActive != true) {
            progressJob = launch { trackProgress() }
        }
        initial = false
        started = true
    }

    fun play() {
        player.playWhenReady = !player.playWhenReady
        showHUD(show = !player.playWhenReady, state = player.playWhenReady)
    }

    fun stop() {
        player.stop()
        player.removeListener(listener)
        progressJob?.cancel()
        started = false
        beforeStart()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initial = true
        started = false
        setRepeat(repeat)
        setVolume(volume)
        beforeStart()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        VideoPlayerHolder.releaseSelf(this)
    }

    internal fun release() {
        stop()
        vp_surface.player = null
        initial = true
    }

    private fun beforeStart() {
        vp_surface.setVisible(false)
        showHUD(show = true, state = null)
    }

    private suspend fun trackProgress() {
        hud.apply {
            while (isActive) {
                delay(200)
                if (player.duration != C.TIME_UNSET) {
                    vp_timebar.setDuration(player.duration)
                    vp_timebar.setBufferedPosition(player.bufferedPosition)
                    if (player.isPlaying) {
                        vp_remained.text = (player.duration - player.currentPosition).toTimeFormat()
                        vp_timebar.setPosition(player.currentPosition)
                    }
                }
            }
        }
    }

    private fun setRepeat(enabled: Boolean) {
        repeat = enabled
        hud.vp_repeat.setImageDrawable(if (repeat) repeatOn else repeatOff)
        if (this::player.isInitialized) {
            player.repeatMode = if (repeat) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
        }
    }

    private fun setVolume(level: Float) {
        volume = level
        if (this::player.isInitialized) {
            player.audioComponent?.volume = volume
        }
        hud.apply {
            val drawable = if (volume > 0) volumeOn else volumeOff
            vp_volume.setImageDrawable(drawable)
        }
    }

    private fun createListener() = object : Player.EventListener, TimeBar.OnScrubListener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_ENDED) {
                showHUD(show = true, state = false)
                player.playWhenReady = false
                started = false
            }
        }

        override fun onScrubStart(timeBar: TimeBar, position: Long) {
            // todo: #76 - show hint with current position
        }

        override fun onScrubMove(timeBar: TimeBar, position: Long) {
            // todo: #76 - show hint with expected position
        }

        override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
            if (!canceled) {
                player.seekTo(position)
                timeBar.setPosition(position)
                started = (player.duration - position) >= 1000
            }
        }

    }

    private fun initControllerListeners() {
        hud.apply {
            vp_veil.setOnClickListener {
                if (initial && !started) {
                    start()
                } else {
                    showHUD(show = !isHudVisible(), state = started && player.playWhenReady)
                }
            }
            vp_play.setOnClickListener { if (!started) start() else play() }
            vp_pause.setOnClickListener { play() }
            vp_repeat.setOnClickListener { setRepeat(!repeat) }
            vp_volume.setOnClickListener { setVolume(if (volume > 0f) 0f else 1f) }
            vp_fullscreen.setOnClickListener { /*todo: #76 - change fullscreen mode */ }
        }
    }

    /**
     * @param show show / hide HUD
     * @param state before start: null, paused: false, playing: true
     */
    private fun showHUD(show: Boolean, state: Boolean?) {
        hud.apply {
            vp_play.setVisible(show && state != true)
            vp_pause.setVisible(show && state == true)

            vp_timebar.setVisible(show && state != null)
            vp_remained.setVisible(show && state != null)

            vp_repeat.setVisible(show)
            vp_volume.setVisible(show)
            vp_fullscreen.setVisible(show)
        }
    }

    private fun View.setVisible(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun isHudVisible() = hud.vp_fullscreen.visibility == View.VISIBLE

}