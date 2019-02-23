package com.e.btex.data

import com.e.btex.util.extensions.applyDispersion

data class Sensors(val temperature: Float,
                   val humidity: Float,
                   val co2: Float,
                   val pm1: Float,
                   val pm10: Float,
                   val pm25: Float,
                   val tvoc: Float){
    companion object {}
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

fun Sensors.Companion.getRandomValues(): Sensors {
    val rand =  Sensors(
        35.applyDispersion(0.3).toFloat(),
        45.applyDispersion(0.3).toFloat(),
        4000.applyDispersion(0.3).toFloat(),
        290.applyDispersion(0.3).toFloat(),
        200.applyDispersion(0.3).toFloat(),
        25.applyDispersion(0.3).toFloat(),
        783.applyDispersion(0.3).toFloat())
    return rand
}