package com.daw

class Synthesizer {
    private val sampleAccumulator = mutableMapOf<Int, Double>()
    
    fun generateSample(activeNotes: List<Pair<Int, Float>>, sampleRate: Int): Float {
        var output = 0.0
        
        for ((note, volume) in activeNotes) {
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
            
            output += wave * volume * 0.2
            sampleAccumulator[note] = notePhase % (2.0 * Math.PI)
        }
        
        return output.coerceIn(-1.0, 1.0).toFloat()
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