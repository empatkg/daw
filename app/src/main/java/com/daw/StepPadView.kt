package com.daw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class StepPadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private var patternManager: PatternManager? = null
    private var onPadClickListener: ((Int, Int, Boolean) -> Unit)? = null
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    private var cellSize = 0
    private var numRows = 4
    private var numCols = 16
    
    fun setPatternManager(manager: PatternManager) {
        this.patternManager = manager
        updatePattern()
    }
    
    fun setOnPadClickListener(listener: (Int, Int, Boolean) -> Unit) {
        this.onPadClickListener = listener
    }
    
    private fun updatePattern() {
        val pattern = patternManager?.currentPattern ?: return
        numRows = pattern.tracks.size
        invalidate()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val pattern = patternManager?.currentPattern ?: return
        val width = width
        val height = height
        
        cellSize = min(width / numCols, height / numRows)
        
        for (row in 0 until numRows) {
            val track = pattern.tracks[row]
            paint.color = track.color
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2f
            
            canvas.drawRect(
                Rect(0, row * cellSize, width, (row + 1) * cellSize),
                paint
            )
            
            paint.textSize = 32f
            paint.textAlign = Paint.Align.CENTER
            paint.color = Color.WHITE
            canvas.drawText(
                track.name.take(3),
                cellSize / 2f,
                row * cellSize + cellSize / 2f + 8,
                paint
            )
            
            for (col in 0 until numCols) {
                val isActive = pattern.steps[row][col]
                if (isActive) {
                    paint.color = track.color
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(
                        col * cellSize + 4f,
                        row * cellSize + 4f,
                        (col + 1) * cellSize - 4f,
                        (row + 1) * cellSize - 4f,
                        paint
                    )
                }
            }
        }
        
        paint.color = Color.GRAY
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 0.5f
        for (i in 0..numCols) {
            canvas.drawLine(i * cellSize.toFloat(), 0f, i * cellSize.toFloat(), height.toFloat(), paint)
        }
        for (i in 0..numRows) {
            canvas.drawLine(0f, i * cellSize.toFloat(), width.toFloat(), i * cellSize.toFloat(), paint)
        }
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val col = (event.x / cellSize).toInt().coerceIn(0, numCols - 1)
            val row = (event.y / cellSize).toInt().coerceIn(0, numRows - 1)
            
            val pattern = patternManager?.currentPattern ?: return true
            val currentState = pattern.steps[row][col]
            onPadClickListener?.invoke(row, col, !currentState)
            invalidate()
            return true
        }
        return super.onTouchEvent(event)
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = numCols * 60
        val desiredHeight = numRows * 60
        
        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)
        
        setMeasuredDimension(width, height)
    }
}