package com.daw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class MixerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View {
    
    private var patternManager: PatternManager? = null
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val sliderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    private var trackHeight = 0
    private var sliderWidth = 200
    private var sliderX = 0
    
    private var touchedTrack = -1
    
    init {
        textPaint.color = Color.WHITE
        textPaint.textSize = 28f
        textPaint.textAlign = Paint.Align.LEFT
        
        sliderPaint.color = Color.LTGRAY
        sliderPaint.style = Paint.Style.STROKE
        sliderPaint.strokeWidth = 2f
    }
    
    fun setPatternManager(manager: PatternManager) {
        this.patternManager = manager
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val pattern = patternManager?.currentPattern ?: return
        val width = width
        val height = height
        
        trackHeight = min(80, height / pattern.tracks.size)
        sliderWidth = (width - 120).coerceAtMost(200)
        sliderX = width - sliderWidth - 20
        
        for ((index, track) in pattern.tracks.withIndices()) {
            val y = index * trackHeight
            
            // Track color indicator
            paint.color = track.color
            paint.style = Paint.Style.FILL
            canvas.drawRect(10f, y.toFloat() + 10, 40f, (y + trackHeight - 10).toFloat(), paint)
            
            // Track name
            textPaint.color = Color.WHITE
            canvas.drawText(track.name, 50f, y + trackHeight / 2f + 8, textPaint)
            
            // Volume slider background
            paint.color = Color.DKGRAY
            canvas.drawRect(
                sliderX.toFloat(),
                y + trackHeight / 2f - 5,
                (sliderX + sliderWidth).toFloat(),
                y + trackHeight / 2f + 5,
                paint
            )
            
            // Volume slider handle
            val thumbX = sliderX + (track.volume * sliderWidth).toInt()
            paint.color = track.color
            canvas.drawCircle(
                thumbX.toFloat(),
                y + trackHeight / 2f,
                12f,
                paint
            )
            
            // Mute/Solo indicators
            textPaint.textSize = 20f
            textPaint.color = if (track.muted) Color.RED else Color.GRAY
            canvas.drawText("M", sliderX + sliderWidth + 30f, y + trackHeight / 2f + 6, textPaint)
            
            textPaint.color = if (track.solo) Color.YELLOW else Color.GRAY
            canvas.drawText("S", sliderX + sliderWidth + 60f, y + trackHeight / 2f + 6, textPaint)
        }
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pattern = patternManager?.currentPattern ?: return true
        val trackIndex = (event.y / trackHeight).toInt().coerceIn(0, pattern.tracks.size - 1)
        
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchedTrack = trackIndex
                updateVolume(event.x, trackIndex)
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchedTrack == trackIndex) {
                    updateVolume(event.x, trackIndex)
                }
            }
            MotionEvent.ACTION_UP -> {
                touchedTrack = -1
            }
        }
        return true
    }
    
    private fun updateVolume(x: Float, trackIndex: Int) {
        val volume = ((x - sliderX) / sliderWidth).coerceIn(0f, 1f)
        patternManager?.currentPattern?.tracks?.get(trackIndex)?.volume = volume
        invalidate()
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val pattern = patternManager?.currentPattern
        val desiredHeight = (pattern?.tracks?.size ?: 4) * 80
        
        setMeasuredDimension(
            resolveSize(400, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }
}