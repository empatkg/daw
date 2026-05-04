package com.daw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min

class PianoRollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private var patternManager: PatternManager? = null
    private var selectedTrack = 0
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    private var cellWidth = 40
    private var cellHeight = 30
    
    private var scrollX = 0
    private var scrollY = 0
    
    fun setPatternManager(manager: PatternManager) {
        this.patternManager = manager
        invalidate()
    }
    
    fun setSelectedTrack(track: Int) {
        selectedTrack = track
        invalidate()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val pattern = patternManager?.currentPattern ?: return
        val width = width
        val height = height
        val notes = 128
        
        cellWidth = max(30, width / 16)
        cellHeight = max(20, height / (notes / 12))
        
        // Draw grid background
        paint.color = Color.rgb(30, 30, 30)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        
        // Draw notes (piano roll style)
        for (note in 0 until 16) {
            val y = height - (note + 1) * cellHeight + scrollY
            if (y < 0 || y > height) continue
            
            paint.color = if (note % 12 == 0) Color.DKGRAY else Color.rgb(50, 50, 50)
            canvas.drawRect(0f, y - cellHeight, width.toFloat(), y.toFloat(), paint)
            
            textPaint.color = Color.LTGRAY
            textPaint.textSize = 24f
            canvas.drawText("C${note / 12}", 5f, y - 5f, textPaint)
        }
        
        // Draw notes
        for (step in 0 until 16) {
            for (note in 0 until 16) {
                val y = height - (note + 1) * cellHeight + scrollY
                if (y < -cellHeight || y > height) continue
                
                val x = step * cellWidth - scrollX
                if (x < 0 || x > width) continue
                
                paint.color = pattern.tracks.getOrNull(selectedTrack)?.color ?: Color.WHITE
                canvas.drawRect(x, y - cellHeight, x + cellWidth - 2, y - 2f, paint)
            }
        }
        
        // Draw current step indicator
        val currentStepX = (patternManager?.currentStep ?: 0) * cellWidth - scrollX
        paint.color = Color.YELLOW
        paint.style = Paint.Style.STROKE
        canvas.drawLine(currentStepX.toFloat(), 0f, currentStepX.toFloat(), height.toFloat(), paint)
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val step = (event.x / cellWidth).toInt().coerceIn(0, 15)
                val note = ((height - event.y) / cellHeight).toInt().coerceIn(0, 15)
                
                patternManager?.apply {
                    val stepIndex = step
                    val trackIndex = selectedTrack
                    if (trackIndex < currentPattern.steps.size) {
                        currentPattern.steps[trackIndex][stepIndex] = 
                            !currentPattern.steps[trackIndex][stepIndex]
                        invalidate()
                    }
                }
            }
        }
        return true
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            resolveSize(800, widthMeasureSpec),
            resolveSize(400, heightMeasureSpec)
        )
    }
}