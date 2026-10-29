package com.milt.miltvplus.ui.player

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.milt.miltvplus.domain.model.PlayableItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PlayerUiState(
    val currentItem: PlayableItem? = null,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val volume: Float = 1.0f,
    val isMuted: Boolean = false,
    val errorMessage: String? = null,
    val isFullscreen: Boolean = false,
    val aspectRatioMode: String = "16:9"
)

class PlayerManager private constructor(context: Context) {
    private val appContext = context.applicationContext
    val exoPlayer: ExoPlayer = ExoPlayer.Builder(appContext).build()

    private val _playerState = MutableStateFlow(PlayerUiState())
    val playerState: StateFlow<PlayerUiState> = _playerState.asStateFlow()

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                val isBuffering = playbackState == Player.STATE_BUFFERING
                val isPlayingWhenReady = exoPlayer.isPlaying
                _playerState.update {
                    it.copy(
                        isBuffering = isBuffering,
                        isPlaying = isPlayingWhenReady,
                        durationMs = exoPlayer.duration.coerceAtLeast(0L),
                        currentPositionMs = exoPlayer.currentPosition.coerceAtLeast(0L)
                    )
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playerState.update { it.copy(isPlaying = isPlaying) }
            }

            override fun onPlayerError(error: PlaybackException) {
                _playerState.update {
                    it.copy(
                        isBuffering = false,
                        isPlaying = false,
                        errorMessage = error.message ?: "Error de reproducción. Reintentando..."
                    )
                }
                // Automatic retry mechanism
                try {
                    exoPlayer.prepare()
                    exoPlayer.play()
                } catch (e: Exception) {
                    // Ignore retry failure
                }
            }
        })
    }

    fun play(item: PlayableItem) {
        try {
            val mediaItem = MediaItem.fromUri(Uri.parse(item.streamUrl))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
            _playerState.update {
                it.copy(
                    currentItem = item,
                    isPlaying = true,
                    errorMessage = null,
                    isBuffering = true
                )
            }
        } catch (e: Exception) {
            _playerState.update {
                it.copy(errorMessage = "No se pudo cargar el stream: ${e.localizedMessage}")
            }
        }
    }

    fun retryPlayback() {
        _playerState.value.currentItem?.let { item ->
            _playerState.update { it.copy(errorMessage = null, isBuffering = true) }
            play(item)
        }
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            if (exoPlayer.playbackState == Player.STATE_ENDED) {
                exoPlayer.seekTo(0)
            }
            exoPlayer.play()
        }
    }

    fun pause() {
        exoPlayer.pause()
    }

    fun resume() {
        exoPlayer.play()
    }

    fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }

    fun seekForward(deltaMs: Long = 10_000L) {
        val target = (exoPlayer.currentPosition + deltaMs).coerceAtMost(exoPlayer.duration.coerceAtLeast(0L))
        exoPlayer.seekTo(target)
    }

    fun seekBackward(deltaMs: Long = 10_000L) {
        val target = (exoPlayer.currentPosition - deltaMs).coerceAtLeast(0L)
        exoPlayer.seekTo(target)
    }

    fun setVolume(volume: Float) {
        exoPlayer.volume = volume
        _playerState.update { it.copy(volume = volume, isMuted = volume == 0f) }
    }

    fun toggleMute() {
        val currentMuted = _playerState.value.isMuted
        if (currentMuted) {
            exoPlayer.volume = 1.0f
            _playerState.update { it.copy(isMuted = false, volume = 1.0f) }
        } else {
            exoPlayer.volume = 0.0f
            _playerState.update { it.copy(isMuted = true, volume = 0.0f) }
        }
    }

    fun setAspectRatio(mode: String) {
        _playerState.update { it.copy(aspectRatioMode = mode) }
    }

    fun setFullscreen(fullscreen: Boolean) {
        _playerState.update { it.copy(isFullscreen = fullscreen) }
    }

    fun release() {
        try {
            exoPlayer.release()
        } catch (e: Exception) {
            // Ignore
        }
    }

    companion object {
        @Volatile
        private var instance: PlayerManager? = null

        fun getInstance(context: Context): PlayerManager {
            return instance ?: synchronized(this) {
                instance ?: PlayerManager(context).also { instance = it }
            }
        }
    }
}
