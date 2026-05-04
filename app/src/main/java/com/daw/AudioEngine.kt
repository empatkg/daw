package com.daw

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AudioEngine(private val context: Context) {
    private val sampleRate = 44100
    private val bufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )
    
    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private var playbackJob: Job? = null
    
    private val synth = Synthesizer()
    private var stepJob: Job? = null
    
    fun playPattern(patternManager: PatternManager) {
        if (isPlaying) return
        
        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .build()
        
        isPlaying = true
        audioTrack?.play()
        
        val stepDurationMs = 150L
        stepJob = CoroutineScope(Dispatchers.Default).launch {
            var currentStep = 0
            while (isPlaying) {
                currentStep = (currentStep + 1) % 16
                kotlinx.coroutines.delay(stepDurationMs)
            }
        }
        
        playbackJob = CoroutineScope(Dispatchers.Default).launch {
            val buffer = ShortArray(bufferSize)
            
            while (isPlaying) {
                val activeNotes = patternManager.getActiveNotes(patternManager.currentStep)
                
                for (i in buffer.indices) {
                    val sample = synth.generateSample(activeNotes, sampleRate)
                    buffer[i] = (sample * Short.MAX_VALUE).toInt().toShort()
                }
                
                audioTrack?.write(buffer, 0, buffer.size)
            }
        }
    }
    
    fun stop() {
        isPlaying = false
        stepJob?.cancel()
        playbackJob?.cancel()
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }
    
    fun startRecording() {
    }
    
    fun release() {
        stop()
    }
}