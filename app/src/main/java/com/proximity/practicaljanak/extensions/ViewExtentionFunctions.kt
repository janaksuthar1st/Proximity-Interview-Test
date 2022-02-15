package com.proximity.practicaljanak.extensions

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.proximity.practicaljanak.R
import com.proximity.practicaljanak.utils.GlideApp
import com.proximity.practicaljanak.utils.GlideUrlWithCache
import java.io.File


fun View.showView() {
    visibility = View.VISIBLE
}

fun View.hideView() {
    visibility = View.GONE
}

fun View.invisibleView() {
    visibility = View.INVISIBLE
}

fun AppCompatImageView.setAlbumArt(
    context: Context,
    url: String?,
    radius: Int = 0,
    placeHolder: Int? = R.drawable.ic_launcher_foreground
) {
    try {
        if (radius != 0) {
            GlideApp.with(context).load(
                if (url.nullSafe()
                        .contains("http")
                ) GlideUrlWithCache(url.nullSafe()) else File(url.nullSafe())
            ).transform(CenterCrop(), RoundedCorners(radius))
                .placeholder(placeHolder!!).error(placeHolder).into(this)
        } else {
            GlideApp.with(context).load(
                if (url.nullSafe()
                        .contains("http")
                ) GlideUrlWithCache(url.nullSafe()) else File(url.nullSafe())
            ).transform(CenterCrop())
                .placeholder(placeHolder!!).error(placeHolder).into(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun AppCompatTextView.setCityName(cityName : String){
    this.text = cityName
}

fun AppCompatTextView.setAQI(aqiQuality : String){
    this.text = this.context.getString(R.string.lbl_current_aqi_value,aqiQuality)
}

fun AppCompatTextView.setQuality(quality : Double){
    val good : LongRange = 0L..50L
    val satisfactory : LongRange = 51L..100L
    val moderate : LongRange = 101L..200L
    val poor : LongRange = 201L..300L
    val veryPoor : LongRange = 301L..400L
    val severe : LongRange = 401L..500L
    when {
        quality.toLong() in good -> {
            this.text = this.context.getText(R.string.lbl_quality_good)
            this.setBackgroundColor(ContextCompat.getColor(this.context,R.color.bgColorGood))
        }
        quality.toLong() in satisfactory -> {
            this.text = this.context.getText(R.string.lbl_quality_satisfactory)
            this.setBackgroundColor(ContextCompat.getColor(this.context,R.color.bgColorSatisfactory))
        }
        quality.toLong() in moderate -> {
            this.text = this.context.getText(R.string.lbl_quality_moderate)
            this.setBackgroundColor(ContextCompat.getColor(this.context,R.color.bgColorModerate))
        }
        quality.toLong() in poor -> {
            this.text = this.context.getText(R.string.lbl_quality_poor)
            this.setBackgroundColor(ContextCompat.getColor(this.context,R.color.bgColorPoor))
        }
        quality.toLong() in veryPoor -> {
            this.text = this.context.getText(R.string.lbl_quality_very_poor)
            this.setBackgroundColor(ContextCompat.getColor(this.context,R.color.bgColorVeryPoor))
        }
        quality.toLong() in severe -> {
            this.text = this.context.getText(R.string.lbl_quality_severe)
            this.setBackgroundColor(ContextCompat.getColor(this.context,R.color.bgColorSevere))
        }
        else -> {
            this.text = this.context.getText(R.string.lbl_quality_na)
            this.setBackgroundColor(ContextCompat.getColor(this.context,R.color.textColorBlack))
        }
    }
}