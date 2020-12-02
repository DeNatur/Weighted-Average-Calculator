package com.szymonstasik.kalkulatorsredniejwazonej.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class StrokeTextView : AppCompatTextView {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {}

    override fun onDraw(canvas: Canvas) {
        val textColor = textColors.defaultColor
        setTextColor(Color.BLACK)
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        super.onDraw(canvas)
        setTextColor(textColor)
        paint.strokeWidth = 0f
        paint.style = Paint.Style.FILL
        super.onDraw(canvas)
    }
}