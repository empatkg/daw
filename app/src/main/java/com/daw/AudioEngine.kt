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
        AudioFormat.CHANNEL_OUT_STEREO,
        AudioFormat.ENCODING_PCM_16BIT
    )
    
    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private var isRecording = false
    private var playbackJob: Job? = null
    
    private val synth = Synthesizer()
    
    fun playPattern(pattern: Pattern) {
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
                    .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .build()
        
        isPlaying = true
        audioTrack?.play()
        
        playbackJob = CoroutineScope(Dispatchers.Default).launch {
            val buffer = ShortArray(bufferSize / 2)
            val stepDuration = sampleRate / 4 // 16th note at 60 BPM
            var stepPosition = 0
            var samplesInStep = 0
            
            while (isPlaying) {
                val patternSteps = pattern.steps
                val activeNotes = mutableListOf<Pair<Int, Float>>()
                
                for ((trackIndex, trackSteps) in patternSteps.withIndex()) {
                    if (pattern.tracks[trackIndex].enabled && 
                        stepPosition < trackSteps.size && trackSteps[stepPosition]) {
                        val note = 60 + trackIndex // C4 base + track offset
                        activeNotes.add(Pair(note, pattern.tracks[trackIndex].volume))
                    }
                }
                
                for (i in buffer.indices step 2) {
                    val sample = synth.generateSample(activeNotes, sampleRate)
                    buffer[i] = (sample * Short.MAX_VALUE).toInt().toShort()
                    buffer[i + 1] = buffer[i]
                }
                
                audioTrack?.write(buffer, 0, buffer.size)
                samplesInStep += buffer.size / 2
                
                if (samplesInStep >= stepDuration) {
                    samplesInStep = 0
                    stepPosition = (stepPosition + 1) % 16
                }
            }
        }
    }
    
    fun stop() {
        isPlaying = false
        playbackJob?.cancel()
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }
    
    fun startRecording() {
        isRecording = true
    }
    
    fun release() {
        stop()
    }
}