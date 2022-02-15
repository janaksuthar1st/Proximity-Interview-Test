package com.proximity.practicaljanak.extensions

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proximity.practicaljanak.models.AirQualityData
import com.proximity.practicaljanak.utils.JLog
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

/**Print log*/
fun Any.showVLog(tag: String, log: String) = JLog.v(tag, log)

fun Any.showELog(tag: String, log: String) = JLog.e(tag, log)

fun Any.showDLog(tag: String, log: String) = JLog.d(tag, log)

fun Any.showILog(tag: String, log: String) = JLog.i(tag, log)

fun Any.showWLog(tag: String, log: String) = JLog.w(tag, log)

/**Show Toast message*/
fun Any.showToast(context: Context, message: String) =
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

fun Any.showLongToast(context: Context, message: String) =
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()

/**Get class name*/
fun Any.getClassName(): String {
    return this::class.java.simpleName
}

/**Null value check*/
fun String?.nullSafe(defaultValue: String = ""): String {
    return this ?: defaultValue
}

fun Int?.nullSafe(defaultValue: Int = 0): Int {
    return this ?: defaultValue
}

fun Float?.nullSafe(defaultValue: Float = 0.0f): Float {
    return this ?: defaultValue
}

fun Long?.nullSafe(defaultValue: Long = 0L): Long {
    return this ?: defaultValue
}

fun Double?.nullSafe(defaultValue: Double = 0.0): Double {
    return this ?: defaultValue
}

fun BigDecimal?.nullSafe(defaultValue: BigDecimal = BigDecimal(0)): BigDecimal {
    return this ?: defaultValue
}

fun Boolean?.nullSafe(defaultValue: Boolean = false): Boolean {
    return this ?: defaultValue
}

fun <T> List<T>?.nullSafe(defaultValue: List<T> = ArrayList()): List<T> {
    return this ?: defaultValue
}

/**LiveData observe single time*/
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun Double.getFormattedDecimalNumber(): String {
    val df = DecimalFormat("#.00")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this)
}

fun String.getAirQualityList(): ArrayList<AirQualityData> {
    return Gson().fromJson(
        this,
        object : TypeToken<ArrayList<AirQualityData?>?>() {}.type
    ) as ArrayList<AirQualityData>
}



