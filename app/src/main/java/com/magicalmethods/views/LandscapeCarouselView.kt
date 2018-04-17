package com.magicalmethods.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import com.synnapps.carouselview.CarouselView

/**
 * Created by ashish kumar
 * on 31-03-2018 | 12:59 PM.
 */
class LandscapeCarouselView : CarouselView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet) : super(context, attr)
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attr, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = width * 9 / 16
        setMeasuredDimension(width, height)
    }
}