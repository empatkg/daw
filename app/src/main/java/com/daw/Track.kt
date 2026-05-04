package com.daw

data class Track(
    val name: String,
    val color: Int,
    var enabled: Boolean = true,
    var volume: Float = 1.0f,
    var muted: Boolean = false,
    var solo: Boolean = false
)