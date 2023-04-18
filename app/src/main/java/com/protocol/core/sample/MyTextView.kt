package com.protocol.core.sample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ReplacementSpan
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class MyTextView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    private var expanded = false
    private val suffix: String = "ALL"
    private val suffixColor: Int = Color.BLACK
    private val suffixSize: Int = 14
    private val suffixBgColor: Int = Color.RED
    private val suffixRadius: Int = 2
    private var originText: CharSequence? = null
    private var endIndex: Int = 0
    private val suffixSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            expanded = !expanded
            setText()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = suffixColor
            ds.isUnderlineText = false
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        originText = text
        setText()
    }

    private fun setText() {
        val layout = layout
        if (layout != null) {
            val lineCount = layout.lineCount
            if (lineCount > 2) {
                endIndex = layout.getLineEnd(if (expanded) lineCount - 1 else 1)
                val ssb = SpannableStringBuilder()
                ssb.append(originText?.subSequence(0, endIndex))
                if (endIndex < (originText?.length ?: 0)) {
                    val suffixSpanBg = RoundBackgroundSpan(suffixBgColor, suffixRadius)
                    ssb.append("...")
                    ssb.append(suffix)
                    val suffixStart = ssb.length - suffix.length
                    ssb.setSpan(
                        suffixSpan,
                        suffixStart,
                        ssb.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    ssb.setSpan(
                        suffixSpanBg,
                        suffixStart,
                        ssb.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    ssb.setSpan(
                        AbsoluteSizeSpan(suffixSize, true),
                        suffixStart,
                        ssb.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                text = ssb
                movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    private inner class RoundBackgroundSpan(private val bgColor: Int, private val radius: Int) :
        ReplacementSpan() {
        override fun getSize(
            paint: Paint,
            text: CharSequence?,
            start: Int,
            end: Int,
            fm: Paint.FontMetricsInt?
        ): Int {
            return (paint.measureText(text, start, end) + radius * 2).toInt()
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
            val rect = RectF(
                x,
                top.toFloat(),
                x + paint.measureText(text, start, end) + radius * 2,
                bottom.toFloat()
            )
            paint.color = bgColor
            canvas.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint)
            paint.color = suffixColor
            if (text != null) {
                canvas.drawText(text, start, end, x + radius, y.toFloat(), paint)
            }
        }
    }
}