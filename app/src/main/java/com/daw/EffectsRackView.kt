package com.daw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class EffectsRackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    private val effects = listOf("Delay", "Reverb", "Filter", "Distortion", "Compressor")
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val width = width
        val height = height
        val itemHeight = height / (effects.size + 1)
        
        paint.color = Color.rgb(40, 40, 40)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        
        textPaint.color = Color.WHITE
        textPaint.textSize = 32f
        
        for (i in effects.indices) {
            val y = (i + 1) * itemHeight - itemHeight / 3
            paint.color = Color.rgb(60, 60, 60)
            canvas.drawRect(10f, y - itemHeight / 2 + 10, width - 10f, y + itemHeight / 2 - 10, paint)
            
            paint.color = Color.CYAN
            canvas.drawRect(15f, y - itemHeight / 2 + 15, 50f, y + itemHeight / 2 - 15, paint)
            
            textPaint.color = Color.WHITE
            canvas.drawText(effects[i], 60f, y + 10, textPaint)
        }
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            resolveSize(400, widthMeasureSpec),
            resolveSize(300, heightMeasureSpec)
        )
    }
}