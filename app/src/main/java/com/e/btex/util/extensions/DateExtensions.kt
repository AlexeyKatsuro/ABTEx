package com.e.btex.util.extensions

import android.app.DatePickerDialog
import android.content.Context
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

typealias DatePickerOptions = DatePickerDialog.() -> Unit

const val defaultDatePattern = "HH:mm:ss dd/MM/yyyy"

fun DatePickerDialog.currentMaxDateOptions() {
    datePicker.maxDate = Calendar.getInstance().timeInMillis
}

fun Context.showDatePickerDialog(
    pattern: String = defaultDatePattern,
    options: DatePickerOptions = { },
    callback: (date: String) -> Unit
) {
    val onDateSelected = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        val calendar = Calendar.getInstance().apply { set(year, month, day) }
        val date = calendar.time.toFormattedStringUTC3(pattern)
        callback(date)
    }

    val year = Calendar.getInstance().get(Calendar.YEAR)
    val month = Calendar.getInstance().get(Calendar.MONTH)
    val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(this, onDateSelected, year, month, day).apply(options).show()
}

fun Fragment.showDatePickerDialog(
    pattern: String = defaultDatePattern,
    options: DatePickerOptions = { },
    callback: (date: String) -> Unit
) {
    requireContext().showDatePickerDialog(pattern, options, callback)
}

fun String.parseDate(pattern: String = defaultDatePattern): Date {
    return SimpleDateFormat(pattern, Locale.getDefault()).parse(this)

}

fun Date.toFormattedStringUTC3(pattern: String = defaultDatePattern): String {
    val offset = TimeZone.getDefault().rawOffset + TimeZone.getDefault().dstSavings
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(time-offset))
}

fun Date.toFormattedString(pattern: String = defaultDatePattern): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(time))
}

