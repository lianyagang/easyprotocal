package com.protocol.core.sample

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val MAX_LINE_COUNT = 2 //最大显示行数
    private var text: String? = null //原文本
    private var foldText: String? = null //折叠文本
    private var unfoldText: String? = null //展开文本
    private var foldTextSize = 0f //折叠文本字体大小
    private var unfoldTextSize = 0f //展开文本字体大小
    private var foldTextColor = Color.BLACK //折叠文本的字体颜色
    private var unfoldTextColor = Color.BLACK //展开文本的字体颜色
    private var foldTextBackground: Drawable? = null //折叠文本的背景色
    private var unfoldTextBackground: Drawable? = null //展开文本的背景色
    private var foldTextRadius = 0f //折叠文本的圆角
    private var unfoldTextRadius = 0f //展开文本的圆角

    init {
        // 获取自定义属性值
//        context.obtainStyledAttributes(attrs, R.styleable.CustomTextView).run {
//            foldText = getString(R.styleable.CustomTextView_foldText)
//            unfoldText = getString(R.styleable.CustomTextView_unfoldText)
//            foldTextSize = getDimension(R.styleable.CustomTextView_foldTextSize, 0f)
//            unfoldTextSize = getDimension(R.styleable.CustomTextView_unfoldTextSize, 0f)
//            foldTextColor = getColor(R.styleable.CustomTextView_foldTextColor, Color.BLACK)
//            unfoldTextColor = getColor(R.styleable.CustomTextView_unfoldTextColor, Color.BLACK)
//            foldTextBackground = getDrawable(R.styleable.CustomTextView_foldTextBackground)
//            unfoldTextBackground = getDrawable(R.styleable.CustomTextView_unfoldTextBackground)
//            foldTextRadius = getDimension(R.styleable.CustomTextView_foldTextRadius, 0f)
//            unfoldTextRadius = getDimension(R.styleable.CustomTextView_unfoldTextRadius, 0f)
//            recycle()
//        }
        //设置最大行数
        maxLines = MAX_LINE_COUNT
        //设置点击事件
        setOnClickListener {
            if (maxLines == MAX_LINE_COUNT) {
                //设置展开文本
                maxLines = Integer.MAX_VALUE
                setText(getUnfoldText())
            } else {
                //设置折叠文本
                maxLines = MAX_LINE_COUNT
                setText(getFoldText())
            }
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        this.text = text.toString()
        super.setText(text, type)
    }

    private val maxLine = 2
    private val expandText = "展开"
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
                ForegroundColorSpan(foldTextColor),
                expandableText.length - expandText!!.length, expandableText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            //设置折叠背景色
            spanString.setSpan(
                RoundCornerBackgroundSpan(Color.RED, 10.0f),
                expandableText.length - expandText!!.length, expandableText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setText(spanString)
        }
    }

    /**
     * 获取折叠文本
     */
    private fun getFoldText(): CharSequence {

        val foldString = "$text...$foldText"
        val spanString = SpannableString(foldString)
        //设置折叠字体大小
        if (foldTextSize > 0) {
            spanString.setSpan(
                AbsoluteSizeSpan(foldTextSize.toInt()),
                foldString.length - foldText!!.length, foldString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        //设置折叠字体颜色
        spanString.setSpan(
            ForegroundColorSpan(foldTextColor),
            foldString.length - foldText!!.length, foldString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //设置折叠背景色
        spanString.setSpan(
            RoundCornerBackgroundSpan(Color.RED, 10.0f),
            foldString.length - foldText!!.length, foldString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spanString
    }

    /**
     * 获取展开文本
     */
    private fun getUnfoldText(): CharSequence {
        if (TextUtils.isEmpty(unfoldText)) {
            unfoldText = "收起"
        }
        val unfoldString = "$text...$unfoldText"
        val spanString = SpannableString(unfoldString)
        //设置展开字体大小
        if (unfoldTextSize > 0) {
            spanString.setSpan(
                AbsoluteSizeSpan(unfoldTextSize.toInt()),
                unfoldString.length - unfoldText!!.length, unfoldString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        //设置展开字体颜色
        spanString.setSpan(
            ForegroundColorSpan(unfoldTextColor),
            unfoldString.length - unfoldText!!.length, unfoldString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //设置展开背景色
        spanString.setSpan(
            RoundCornerBackgroundSpan(Color.RED, 10.0f),
            unfoldString.length - unfoldText!!.length, unfoldString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spanString
    }


}