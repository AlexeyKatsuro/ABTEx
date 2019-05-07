package com.e.btex.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.e.btex.data.SensorsType
import com.e.btex.data.protocol.RemoteData
import com.e.btex.util.extensions.applyDispersion
import kotlinx.android.parcel.Parcelize

@Entity
data class Sensors(
    @PrimaryKey
    var id: Int,
    val time: Long,
    val temperature: Float,
    val humidity: Float,
    val co2: Float,
    val pm1: Float,
    val pm10: Float,
    val pm25: Float,
    val tvoc: Float
){


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

fun Sensors.Companion.getRandomValues(id: Int, time: Long): Sensors {
    return Sensors(id,
        time,
        35.applyDispersion(0.3).toFloat(),
        45.applyDispersion(0.3).toFloat(),
        4000.applyDispersion(0.3).toFloat(),
        290.applyDispersion(0.3).toFloat(),
        200.applyDispersion(0.3).toFloat(),
        25.applyDispersion(0.3).toFloat(),
        783.applyDispersion(0.3).toFloat()
    )

}