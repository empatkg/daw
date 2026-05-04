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
        invalidate()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val pattern = patternManager?.currentPattern ?: return
        val width = width.toFloat()
        val height = height.toFloat()
        val rows = pattern.tracks.size
        
        cellSize = min(min(50, (width / 18).toInt()), (height / (rows + 1)).toInt())
        
        paint.color = Color.rgb(40, 40, 40)
        canvas.drawRect(0f, 0f, width, cellSize * 1.5f, paint)
        
        for (step in 0 until 16) {
            textPaint.color = if (step == currentStep) Color.YELLOW else Color.LTGRAY
            val x = (step + 1) * cellSize + cellSize / 2f
            canvas.drawText(step.toString(), x, cellSize / 2f + 8, textPaint)
        }
        
        val startY = cellSize * 1.5f
        for (rowIndex in pattern.tracks.indices) {
            val track = pattern.tracks[rowIndex]
            val y = startY + rowIndex * cellSize + cellSize / 2
            
            paint.color = track.color
            canvas.drawRect(0f, y - cellSize / 2 + 4, 8f, y + cellSize / 2 - 4, paint)
            
            textPaint.color = Color.WHITE
            textPaint.textSize = 20f
            canvas.drawText(track.name.take(6), cellSize / 2f, y + 6, textPaint)
            
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
        
        val stepX = (currentStep + 1) * cellSize + cellSize / 2
        paint.color = Color.YELLOW
        paint.strokeWidth = 2f
        canvas.drawLine(stepX.toFloat(), startY, stepX.toFloat(), height, paint)
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