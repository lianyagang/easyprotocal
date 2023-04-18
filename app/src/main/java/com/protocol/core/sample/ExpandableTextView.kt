package com.protocol.core.sample

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class ExpandableTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val maxLine = 2
    private val expandText = "展开"
    private var expandable = false
    private var expandTextPaint = TextPaint()
    private var expandTextRect = RectF()
    private var expandTextHeight = 0f
    private var expandTextWidth = 0f
    private var expandTextBgRect = RectF()
    private var expandTextBgPaint = Paint()
    private var expandTextBgCornerRadius = 0f
    private var expandTextColor = Color.BLACK
    private var expandTextBgColor = Color.WHITE

    init {
        // 初始化展开文本的画笔
        expandTextPaint.isAntiAlias = true
        expandTextPaint.color = Color.BLACK
        expandTextPaint.textSize = textSize
        // 设置点击事件监听器
        setOnClickListener {
            if (expandable) {
                expandable = false
                // 调用父类的setText方法设置完整文本
                super.setText(text, BufferType.NORMAL)
            }
        }
    }

    // 设置展开文本的字体大小
    fun setExpandTextSize(size: Float) {
        expandTextPaint.textSize = size
        requestLayout()
    }

    // 设置展开文本的字体颜色
    fun setExpandTextColor(color: Int) {
        expandTextColor = color
        invalidate()
    }

    // 设置展开文本的背景颜色
    fun setExpandTextBgColor(color: Int) {
        expandTextBgColor = color
        invalidate()
    }

    // 设置展开文本的背景圆角
    fun setExpandTextBgCornerRadius(radius: Float) {
        expandTextBgCornerRadius = radius
        invalidate()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        // 调用父类的setText方法设置文本
        super.setText(text, type)
        // 重新计算文本的行数和高度
        val layout = layout
        if (layout != null && layout.lineCount > maxLine) {
            val endIndex = layout.getLineEnd(maxLine - 1)
            val truncatedText = text?.subSequence(0, endIndex - 1)
            val expandableText = "$truncatedText... $expandText"
            val expandLayout = StaticLayout(
                expandableText, paint, width,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false
            )
            expandTextWidth = paint.measureText(expandText)
            expandTextHeight = expandLayout.height.toFloat()
            expandTextRect = RectF(
                width - expandTextWidth, height - expandTextHeight,
                width.toFloat(), height.toFloat()
            )
            expandTextBgRect = RectF(
                expandTextRect.left - 8, expandTextRect.top - 4,
                expandTextRect.right + 8, expandTextRect.bottom + 4
            )
            expandable = true
        }
    }
}