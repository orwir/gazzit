package orwir.videoplayer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.ExoPlayer

fun LifecycleOwner.bindPlayer(player: ExoPlayer) {
    lifecycle.addObserver(PlayerObserver(player))
}

class PlayerObserver(private val player: ExoPlayer) : LifecycleObserver {

    private var playOnResume: Boolean = false

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        player.playWhenReady = playOnResume
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        playOnResume = player.playWhenReady
        player.playWhenReady = false
    }

}