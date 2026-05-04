package com.daw

data class Pattern(
    val name: String = "Pattern 1",
    val tracks: MutableList<Track> = mutableListOf(
        Track("Kick", 0xFFE91E63.toInt()),
        Track("Snare", 0xFF2196F3.toInt()),
        Track("Hi-Hat", 0xFF4CAF50.toInt()),
        Track("Clap", 0xFFFFC107.toInt())
    ),
    val steps: MutableList<BooleanArray> = mutableListOf(
        BooleanArray(16),
        BooleanArray(16),
        BooleanArray(16),
        BooleanArray(16)
    )
)