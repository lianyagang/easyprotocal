package com.protocol.core.sample

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class ExpandTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
    }

    private val maxLine = 2
    private val expandText = "ALL"
    private val ellipText = "... "

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 重新计算文本的行数和高度
        val layout = layout
        if (layout != null && layout.lineCount > maxLine) {
            val endIndex = layout.getLineEnd(maxLine - 1)
            val truncatedText =
                text?.subSequence(0, endIndex - expandText.length - ellipText.length)
            val expandableText = "$truncatedText$ellipText$expandText"
            val spanString = SpannableString(expandableText)
            //设置折叠字体颜色
            spanString.setSpan(
                ForegroundColorSpan(Color.WHITE),
                expandableText.length - expandText.length, expandableText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            //设置折叠背景色
            spanString.setSpan(
                RoundCornerBackgroundSpan(Color.RED, 10.0f),
                expandableText.length - expandText.length, expandableText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text = spanString
        }
    }
}