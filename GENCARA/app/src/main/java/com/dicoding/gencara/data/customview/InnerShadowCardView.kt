package com.dicoding.gencara.data.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.cardview.widget.CardView

class InnerShadowCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val rectF = RectF()
    private val cornerRadius = 10f

    init {
        cardElevation = 0f
        maxCardElevation = 0f
        useCompatPadding = false
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()

        // Draw the shadow first
        rectF.set(0f, 0f, width, height)
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, shadowPaint)

        // Clear the shadow area
        canvas.saveLayer(rectF, null)
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, backgroundPaint)

        // Draw the content of the card
        super.onDraw(canvas)

        canvas.restore()
    }
}