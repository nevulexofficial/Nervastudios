package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.*
import kotlin.math.PI
import kotlin.math.sin

/**
 * NervaAudioEngine manages real audio playback and procedural videogame soundtracks for NERVA FC.
 * Supports:
 *  - Custom streaming audio URLs / MP3s via Android's MediaPlayer
 *  - High-performance synthesized videogame soundtrack tracks via AudioTrack (zero external files, works offline & in browser emulator)
 *  - Stadium sound effects: Referee Whistle, Goal Foghorn, Crowd Cheering
 */
object NervaAudioEngine {
    private const val TAG = "NervaAudioEngine"
    private const val SAMPLE_RATE = 22050

    val PRESET_TRACKS = listOf(
        "Himno Nerva Champions (Épica Sinfónica)",
        "Electro Matchday Hype (Trap & Synth Bass)",
        "Retro Stadium 16-Bit (Arcade Chiptune)",
        "Samba de Janeiro (Carnaval & Victoria)"
    )

    @Volatile
    var isPlaying: Boolean = false
        private set

    @Volatile
    var currentTrack: Int = 0
        private set

    @Volatile
    var volume: Float = 0.75f
        private set

    @Volatile
    var customUrl: String = ""
        private set

    private var mediaPlayer: MediaPlayer? = null
    private var synthTrack: AudioTrack? = null
    private var synthJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    @Volatile
    private var appContext: android.content.Context? = null

    fun initialize(context: android.content.Context) {
        appContext = context.applicationContext
    }

    fun setMusicVolume(vol: Float) {
        val clamped = vol.coerceIn(0f, 1f)
        volume = clamped
        try {
            mediaPlayer?.setVolume(clamped, clamped)
            synthTrack?.setVolume(clamped)
        } catch (_: Exception) {}
    }

    fun toggleMusic() {
        if (isPlaying) {
            pauseMusic()
        } else {
            playMusic(currentTrack, customUrl)
        }
    }

    fun playMusic(trackIndex: Int = currentTrack, url: String = customUrl) {
        currentTrack = trackIndex
        customUrl = url
        isPlaying = true

        stopInternal()

        if (url.isNotBlank() && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("content://") || url.startsWith("file://"))) {
            playStreamUrl(url)
        } else {
            playSynthesizedTrack(trackIndex)
        }
    }

    fun pauseMusic() {
        isPlaying = false
        stopInternal()
    }

    fun nextTrack() {
        val next = (currentTrack + 1) % PRESET_TRACKS.size
        playMusic(next, "")
    }

    fun selectPreset(index: Int) {
        val validIndex = index.coerceIn(0, PRESET_TRACKS.size - 1)
        playMusic(validIndex, "")
    }

    fun playCustomAudio(url: String) {
        if (url.isNotBlank()) {
            playMusic(PRESET_TRACKS.size, url)
        }
    }

    private fun playStreamUrl(url: String) {
        scope.launch(Dispatchers.IO) {
            try {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .build()
                    )
                    if (url.startsWith("content://") && appContext != null) {
                        setDataSource(appContext!!, android.net.Uri.parse(url))
                    } else if (url.startsWith("file://")) {
                        setDataSource(url.removePrefix("file://"))
                    } else {
                        setDataSource(url)
                    }
                    isLooping = true
                    setVolume(volume, volume)
                    setOnPreparedListener { mp ->
                        if (isPlaying) mp.start()
                    }
                    setOnErrorListener { _, _, _ ->
                        // Fallback to synth if stream fails
                        scope.launch(Dispatchers.Main) {
                            playSynthesizedTrack(0)
                        }
                        true
                    }
                    prepareAsync()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to stream audio: $url", e)
                playSynthesizedTrack(0)
            }
        }
    }

    private fun playSynthesizedTrack(trackIndex: Int) {
        synthJob?.cancel()
        synthJob = scope.launch(Dispatchers.Default) {
            val bufferSize = AudioTrack.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(4096)

            val track = AudioTrack(
                AudioManager.STREAM_MUSIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM
            )
            synthTrack = track
            try {
                track.setVolume(volume)
                track.play()
            } catch (_: Exception) {
                return@launch
            }

            val buffer = ShortArray(bufferSize)
            var sampleIndex = 0L

            // Note frequencies for different procedural tracks
            val chords = when (trackIndex % 4) {
                0 -> listOf(261.63, 329.63, 392.00, 523.25, 440.00, 349.23) // C Major / A Minor Epic
                1 -> listOf(146.83, 174.61, 220.00, 293.66, 110.00, 82.41)   // D Minor Electro Bass
                2 -> listOf(329.63, 392.00, 493.88, 659.25, 587.33, 440.00) // E Minor 16-Bit Chiptune
                else -> listOf(196.00, 246.94, 293.66, 392.00, 440.00, 329.63) // G Major Samba
            }

            val tempo = when (trackIndex % 4) {
                1 -> 0.14 // Fast electro
                2 -> 0.11 // Rapid chiptune
                3 -> 0.16 // Bouncy samba
                else -> 0.22 // Majestic march
            }

            val samplesPerBeat = (SAMPLE_RATE * tempo).toInt()

            while (isActive && isPlaying) {
                for (i in buffer.indices) {
                    val beatIndex = ((sampleIndex / samplesPerBeat) % chords.size).toInt()
                    val baseFreq = chords[beatIndex]
                    val beatTime = (sampleIndex % samplesPerBeat).toDouble() / SAMPLE_RATE

                    // Envelope: fast attack, natural decay
                    val env = (1.0 - (beatTime / tempo)).coerceIn(0.05, 1.0)

                    val t = sampleIndex.toDouble() / SAMPLE_RATE
                    val wave = when (trackIndex % 4) {
                        0 -> {
                            // Epic brass synth: Fundamental + 2nd + 3rd harmonic
                            sin(2 * PI * baseFreq * t) * 0.55 +
                                    sin(2 * PI * baseFreq * 1.5 * t) * 0.25 +
                                    sin(2 * PI * (baseFreq * 0.5) * t) * 0.20
                        }
                        1 -> {
                            // Electro Bass pulse: square-ish saw wave
                            val p = (t * baseFreq) % 1.0
                            val saw = (p * 2.0 - 1.0) * 0.6
                            val kick = if (beatTime < 0.08) sin(2 * PI * (120 - beatTime * 900) * beatTime) * 0.8 else 0.0
                            saw * 0.6 + kick * 0.4
                        }
                        2 -> {
                            // 16-bit square wave with rapid arpeggio
                            val arpFreq = baseFreq * (if ((sampleIndex / (samplesPerBeat / 4)) % 2 == 0L) 1.0 else 1.5)
                            val square = if (sin(2 * PI * arpFreq * t) > 0) 0.5 else -0.5
                            square
                        }
                        else -> {
                            // Samba rhythm + bright chime
                            val chime = sin(2 * PI * baseFreq * t) * 0.6 + sin(2 * PI * baseFreq * 2.0 * t) * 0.3
                            val snare = if (beatTime > 0.10 && beatTime < 0.14) (Math.random() * 2.0 - 1.0) * 0.4 else 0.0
                            chime + snare
                        }
                    }

                    val sampleValue = (wave * env * 24000.0 * volume).toInt().coerceIn(-32767, 32767).toShort()
                    buffer[i] = sampleValue
                    sampleIndex++
                }

                try {
                    track.write(buffer, 0, buffer.size)
                } catch (_: Exception) {
                    break
                }
            }

            try {
                track.stop()
                track.release()
            } catch (_: Exception) {}
        }
    }

    /**
     * Plays referee whistle sound effect.
     */
    fun playWhistle() {
        scope.launch(Dispatchers.Default) {
            playToneBurst(frequencies = listOf(2800.0, 3100.0), durationMs = 350, isDualTone = true)
        }
    }

    /**
     * Plays stadium goal foghorn sound effect.
     */
    fun playGoalHorn() {
        scope.launch(Dispatchers.Default) {
            playToneBurst(frequencies = listOf(110.0, 165.0, 220.0), durationMs = 800, isDualTone = true)
        }
    }

    /**
     * Plays roaring stadium crowd cheer.
     */
    fun playCrowdCheer() {
        scope.launch(Dispatchers.Default) {
            playNoiseBurst(durationMs = 900)
        }
    }

    private suspend fun playToneBurst(frequencies: List<Double>, durationMs: Int, isDualTone: Boolean) {
        try {
            val count = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
            val track = AudioTrack(
                AudioManager.STREAM_MUSIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                count * 2,
                AudioTrack.MODE_STATIC
            )
            val buffer = ShortArray(count)
            for (i in 0 until count) {
                val t = i.toDouble() / SAMPLE_RATE
                val env = sin(PI * (i.toDouble() / count)) // bell curve
                var wave = 0.0
                for (f in frequencies) {
                    wave += sin(2 * PI * f * t)
                }
                wave /= frequencies.size
                buffer[i] = (wave * env * 28000.0 * volume.coerceAtLeast(0.4f)).toInt().toShort()
            }
            track.write(buffer, 0, count)
            track.play()
            delay(durationMs.toLong() + 50)
            track.release()
        } catch (_: Exception) {}
    }

    private suspend fun playNoiseBurst(durationMs: Int) {
        try {
            val count = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
            val track = AudioTrack(
                AudioManager.STREAM_MUSIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                count * 2,
                AudioTrack.MODE_STATIC
            )
            val buffer = ShortArray(count)
            var prev = 0.0
            for (i in 0 until count) {
                val env = sin(PI * (i.toDouble() / count))
                // Low-pass filtered noise
                val white = (Math.random() * 2.0 - 1.0)
                prev = prev * 0.85 + white * 0.15
                buffer[i] = (prev * env * 25000.0 * volume.coerceAtLeast(0.4f)).toInt().toShort()
            }
            track.write(buffer, 0, count)
            track.play()
            delay(durationMs.toLong() + 50)
            track.release()
        } catch (_: Exception) {}
    }

    private fun stopInternal() {
        synthJob?.cancel()
        synthJob = null
        try {
            synthTrack?.stop()
            synthTrack?.release()
        } catch (_: Exception) {}
        synthTrack = null

        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
    }

    fun release() {
        isPlaying = false
        stopInternal()
        scope.cancel()
    }
}
