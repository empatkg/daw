package com.daw

class Synthesizer {
    private val sampleAccumulator = mutableMapOf<Int, Double>()
    
    data class SynthParams(
        val waveType: Int = 0,
        val attack: Float = 0.01f,
        val decay: Float = 0.1f,
        val sustain: Float = 0.7f,
        val release: Float = 0.2f
    )
    
    fun generateSample(notes: List<Pair<Int, Float>>, sampleRate: Int): Float {
        var output = 0.0
        
        for ((note, volume) in notes) {
            val frequency = 440.0 * Math.pow(2.0, (note - 69) / 12.0)
            val phaseIncrement = 2.0 * Math.PI * frequency / sampleRate
            
            var notePhase = sampleAccumulator[note] ?: 0.0
            notePhase += phaseIncrement
            
            val wave = when (note % 4) {
                0 -> Math.sin(notePhase)
                1 -> squareWave(notePhase)
                2 -> sawtoothWave(notePhase)
                else -> triangleWave(notePhase)
            }
            
            val drumSound = when (note % 4) {
                0 -> kickDrum(notePhase, frequency)
                1 -> snareDrum(notePhase)
                2 -> hiHat(notePhase)
                else -> clap(notePhase)
            }
            
            val sound = if (note < 40) drumSound else wave
            output += sound * volume * 0.3
            sampleAccumulator[note] = notePhase % (2.0 * Math.PI)
        }
        
        return output.coerceIn(-1.0, 1.0).toFloat()
    }
    
    private fun kickDrum(phase: Double, freq: Double): Double {
        return Math.sin(phase * freq / 20) * Math.exp(-phase * 10)
    }
    
    private fun snareDrum(phase: Double): Double {
        val noise = (Math.random() * 2 - 1) * 0.5
        return noise * Math.exp(-phase * 20)
    }
    
    private fun hiHat(phase: Double): Double {
        val noise = (Math.random() * 2 - 1)
        return noise * Math.exp(-phase * 100)
    }
    
    private fun clap(phase: Double): Double {
        val noise = (Math.random() * 2 - 1) * 0.3
        return noise * Math.exp(-phase * 30)
    }
    
    private fun squareWave(phase: Double): Double {
        return if (phase % (2 * Math.PI) < Math.PI) 1.0 else -1.0
    }
    
    private fun sawtoothWave(phase: Double): Double {
        return 2.0 * (phase % (2 * Math.PI)) / (2 * Math.PI) - 1.0
    }
    
    private fun triangleWave(phase: Double): Double {
        val p = phase % (2 * Math.PI)
        return if (p < Math.PI) {
            4.0 * p / Math.PI - 1.0
        } else {
            4.0 * (p - Math.PI) / Math.PI - 1.0
        }
    }
}