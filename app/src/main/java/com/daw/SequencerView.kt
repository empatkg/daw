package com.daw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class SequencerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private var patternManager: PatternManager? = null
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var cellSize = 40
    
    private var currentStep = 0
    private val stepAnimator = object : Runnable {
        override fun run() {
            currentStep = (currentStep + 1) % 16
            invalidate()
            postDelayed(this, 150)
        }
    }
    
    init {
        textPaint.color = Color.GRAY
        textPaint.textSize = 24f
        textPaint.textAlign = Paint.Align.CENTER
    }
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        post(stepAnimator)
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(stepAnimator)
    }
    
    fun setPatternManager(manager: PatternManager) {
        this.patternManager = manager
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val pattern = patternManager?.currentPattern ?: return
        val width = width
        val height = height
        val rows = pattern.tracks.size
        
        cellSize = min(50, min(width / 18, height / (rows + 1)))
        
        for (step in 0 until 16) {
            textPaint.color = if (step == currentStep) Color.YELLOW else Color.GRAY
            canvas.drawText(step.toString(), (step + 1) * cellSize + cellSize / 2f, 30f, textPaint)
        }
        
        for (rowIndex in pattern.tracks.indices) {
            val track = pattern.tracks[rowIndex]
            val y = (rowIndex + 1) * cellSize + 30
            
            textPaint.color = Color.WHITE
            canvas.drawText(track.name.take(4), cellSize / 2f, y + cellSize / 2f + 8, textPaint)
            
            for (step in 0 until 16) {
                val x = (step + 1) * cellSize + cellSize / 2
                
                val isActive = pattern.steps[rowIndex][step]
                paint.color = if (isActive) track.color else Color.DKGRAY
                paint.style = if (isActive) Paint.Style.FILL else Paint.Style.STROKE
                
                paint.strokeWidth = if (step == currentStep) 3f else 1f
                
                canvas.drawRect(
                    x - cellSize / 3f,
                    y - cellSize / 3f,
                    x + cellSize / 3f,
                    y + cellSize / 3f,
                    paint
                )
            }
        }
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val pattern = patternManager?.currentPattern
        val rows = (pattern?.tracks?.size ?: 4) + 1
        val desiredHeight = rows * 70
        
        setMeasuredDimension(
            resolveSize(800, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }
}