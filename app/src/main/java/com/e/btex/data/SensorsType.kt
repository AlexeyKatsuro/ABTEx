package com.e.btex.data

import android.content.Context
import com.e.btex.R

enum class SensorsType {
    temperature,
    humidity,
    co2,
    pm1,
    pm10,
    pm25,
    tvoc,
}

fun SensorsType.getString(context: Context): String {
    return when(this){
        SensorsType.temperature -> context.getString(R.string.temperature)
        SensorsType.humidity -> context.getString(R.string.humidity)
        SensorsType.co2 -> context.getString(R.string.co2)
        SensorsType.pm1 -> context.getString(R.string.pm1)
        SensorsType.pm10 -> context.getString(R.string.pm10)
        SensorsType.pm25 -> context.getString(R.string.pm25)
        SensorsType.tvoc -> context.getString(R.string.tvoc)
    }
}