package com.protocol.core.sample

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan

class RoundCornerBackgroundSpan(color: Int, private var radius: Float) : ReplacementSpan() {

    private var size = 0
    private var bgColor = color
    private var bgRect = RectF()

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        if (text.isNullOrEmpty()) {
            return 0
        }

        size = (paint.measureText(text, start, end) + 2 * radius).toInt()
        return size
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        if (text.isNullOrEmpty()) {
            return
        }

        // 保存文字颜色
        val color = paint.color
        // 设置背景颜色
        paint.color = bgColor
        paint.isAntiAlias = true
        // 设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        bgRect = RectF(x, y + paint.ascent(), x + size, y + paint.descent())
        canvas.drawRoundRect(bgRect, radius, radius, paint)
        // 恢复画笔的文字颜色
        paint.color = color
        canvas.drawText(text, start, end, x + radius, y.toFloat(), paint)
    }

}