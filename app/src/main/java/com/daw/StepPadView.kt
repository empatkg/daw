package com.daw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
        val width = width.toFloat()
        val height = height.toFloat()
        
        cellSize = min(width / numCols, height / numRows).toInt()
        
        for (row in 0 until numRows) {
            val track = pattern.tracks[row]
            val rowY = row * cellSize
            
            paint.color = track.color
            canvas.drawRect(0f, rowY.toFloat(), cellSize + 40f, rowY + cellSize, paint)
            
            paint.textSize = 24f
            paint.textAlign = Paint.Align.LEFT
            paint.color = Color.WHITE
            canvas.drawText(track.name, 8f, rowY + cellSize / 2f + 8, paint)
            
            for (col in 0 until numCols) {
                val isActive = pattern.steps[row][col]
                val x = (col + 1) * cellSize
              
                if (isActive) {
                    paint.color = track.color
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(
                        x + 4f, rowY + 4f,
                        x + cellSize - 4f, rowY + cellSize - 4f,
                        paint
                    )
                } else {
                    paint.color = Color.DKGRAY
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = 1f
                    canvas.drawRect(
                        x.toFloat(), rowY.toFloat(),
                        x + cellSize, rowY + cellSize,
                        paint
                    )
                }
            }
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
        val desiredHeight = numRows * 80
        
        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)
        
        setMeasuredDimension(width, height)
    }
}