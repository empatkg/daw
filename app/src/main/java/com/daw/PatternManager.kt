package com.daw

class PatternManager {
    private val patterns = mutableListOf<Pattern>()
    var currentPatternIndex = 0
    
    init {
        patterns.add(Pattern())
    }
    
    val currentPattern: Pattern get() = patterns[currentPatternIndex]
    
    fun addTrack() {
        val pattern = currentPattern
        pattern.tracks.add(Track("Track ${pattern.tracks.size + 1}", getRandomColor()))
        pattern.steps.add(BooleanArray(16))
    }
    
    fun setStep(trackIndex: Int, stepIndex: Int, active: Boolean) {
        if (trackIndex < currentPattern.steps.size && stepIndex < 16) {
            currentPattern.steps[trackIndex][stepIndex] = active
        }
    }
    
    private fun getRandomColor(): Int {
        val colors = listOf(
            0xFFE91E63.toInt(),
            0xFF2196F3.toInt(),
            0xFF4CAF50.toInt(),
            0xFFFFC107.toInt(),
            0xFF9C27B0.toInt(),
            0xFF00BCD4.toInt(),
            0xFFFF5722.toInt(),
            0xFF795548.toInt()
        )
        return colors.random()
    }
}