package com.e.btex.data.entity

import android.content.Context
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.e.btex.R
import com.e.btex.data.SensorsType
import com.e.btex.util.extensions.applyDispersion
import com.e.btex.util.extensions.toFormattedStringUTC3
import kotlinx.android.parcel.IgnoredOnParcel
import java.util.Date

@Entity
data class Sensors(
    @PrimaryKey
    var id: Int,
    val timeSeconds: Int, //seconds
    val temperature: Float,
    val humidity: Float,
    val co2: Float,
    val pm1: Float,
    val pm10: Float,
    val pm25: Float,
    val tvoc: Float
) {

    @IgnoredOnParcel
    @Ignore
    val timeMillis: Long = timeSeconds*1000L

    @IgnoredOnParcel
    @Ignore
    val timeText: String = Date(timeMillis).toFormattedStringUTC3()



    companion object
}

fun Sensors.getSensorMap(): Map<SensorsType, Float> {
    return mapOf(
        SensorsType.temperature to temperature,
        SensorsType.humidity to humidity,
        SensorsType.co2 to co2,
        SensorsType.pm1 to pm1,
        SensorsType.pm10 to pm10,
        SensorsType.pm25 to pm25,
        SensorsType.tvoc to tvoc
    )
}

fun Sensors.getSensorValue(type: SensorsType): Float {
    return when (type) {
        SensorsType.temperature -> temperature
        SensorsType.humidity -> humidity
        SensorsType.co2 -> co2
        SensorsType.pm1 -> pm1
        SensorsType.pm10 -> pm10
        SensorsType.pm25 -> pm25
        SensorsType.tvoc -> tvoc
    }
}

fun Sensors.Companion.getRandomValues(id: Int, time: Int): Sensors {
    return Sensors(
        id,
        time,
        25.applyDispersion(0.3).toFloat(),
        45.applyDispersion(0.3).toFloat(),
        1000.applyDispersion(0.3).toFloat(),
        290.applyDispersion(0.3).toFloat(),
        200.applyDispersion(0.3).toFloat(),
        25.applyDispersion(0.3).toFloat(),
        783.applyDispersion(0.3).toFloat()
    )

}

fun SensorsType.getStringWithUnits(context: Context): String {
    return "${getString(context)} (${getUnitsString(context)})"
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

fun SensorsType.getUnitsString(context: Context): String {
    return when(this){
        SensorsType.temperature -> context.getString(R.string.temperature_units)
        SensorsType.humidity -> context.getString(R.string.humidity_units)
        SensorsType.co2 -> context.getString(R.string.co2_units)
        SensorsType.pm1 -> context.getString(R.string.pm1_units)
        SensorsType.pm10 -> context.getString(R.string.pm10_units)
        SensorsType.pm25 -> context.getString(R.string.pm25_units)
        SensorsType.tvoc -> context.getString(R.string.tvoc_units)
    }
}