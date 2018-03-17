package com.example.piyush.magicalmethods.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by ashish kumar
 * on 17-03-2018 | 07:26 PM.
 */
class LandscapeImageView : ImageView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = width * 9 / 16
        setMeasuredDimension(width, height)
    }
}