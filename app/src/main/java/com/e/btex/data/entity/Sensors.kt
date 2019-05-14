package com.e.btex.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.e.btex.data.SensorsType
import com.e.btex.util.extensions.applyDispersion
import com.e.btex.util.extensions.toFormattedStringUTC3
import kotlinx.android.parcel.IgnoredOnParcel
import java.util.Date

@Entity
data class Sensors(
    @PrimaryKey
    var id: Int,
    val time: Int, //seconds
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
    val timeText: String = Date(time*1000L).toFormattedStringUTC3()

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