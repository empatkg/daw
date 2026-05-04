package com.daw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class TransportView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    interface TransportListener {
        fun onPlay()
        fun onStop()
        fun onRecord()
    }
    
    private var listener: TransportListener? = null
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val buttonPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    
    private var playButtonRect = RectF()
    private var stopButtonRect = RectF()
    private var recordButtonRect = RectF()
    
    private var isPlaying = false
    
    fun setOnTransportListener(listener: TransportListener) {
        this.listener = listener
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerY = height / 2f
        val buttonRadius = (height * 0.35).coerceAtMost(width * 0.1f)
        
        val spacing = width / 4f
        
        playButtonRect.set(spacing - buttonRadius, centerY - buttonRadius, 
                          spacing + buttonRadius, centerY + buttonRadius)
        
        buttonPaint.color = if (isPlaying) Color.GRAY else Color.GREEN
        buttonPaint.style = Paint.Style.FILL
        canvas.drawOval(playButtonRect, buttonPaint)
        
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        path.reset()
        path.moveTo(spacing - buttonRadius / 2, centerY - buttonRadius / 2)
        path.lineTo(spacing - buttonRadius / 2, centerY + buttonRadius / 2)
        path.lineTo(spacing + buttonRadius / 2, centerY)
        path.close()
        canvas.drawPath(path, paint)
        
        val stopX = spacing * 2
        stopButtonRect.set(stopX - buttonRadius, centerY - buttonRadius,
                          stopX + buttonRadius, centerY + buttonRadius)
        
        buttonPaint.color = Color.RED
        canvas.drawOval(stopButtonRect, buttonPaint)
        
        paint.color = Color.WHITE
        canvas.drawRect(
            stopX - buttonRadius * 0.5f, centerY - buttonRadius * 0.5f,
            stopX + buttonRadius * 0.5f, centerY + buttonRadius * 0.5f,
            paint
        )
        
        val recordX = spacing * 3
        recordButtonRect.set(recordX - buttonRadius, centerY - buttonRadius,
                             recordX + buttonRadius, centerY + buttonRadius)
        
        buttonPaint.color = Color.RED
        canvas.drawOval(recordButtonRect, buttonPaint)
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            when {
                playButtonRect.contains(event.x, event.y) -> {
                    isPlaying = true
                    listener?.onPlay()
                    invalidate()
                }
                stopButtonRect.contains(event.x, event.y) -> {
                    isPlaying = false
                    listener?.onStop()
                    invalidate()
                }
                recordButtonRect.contains(event.x, event.y) -> {
                    listener?.onRecord()
                }
            }
            return true
        }
        return super.onTouchEvent(event)
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            resolveSize(400, widthMeasureSpec),
            resolveSize(100, heightMeasureSpec)
        )
    }
}