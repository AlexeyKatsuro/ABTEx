package com.e.btex.data

import com.e.btex.util.extensions.applyDispersion

data class Sensors(val temperature: Number,
                   val humidity: Number,
                   val co2: Number,
                   val pm1: Number,
                   val pm10: Number,
                   val pm25: Number,
                   val tvoc: Number){
    companion object {}
}

fun Sensors.getSensorMap(): Map<SensorsType, Number> {
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
        35.applyDispersion(0.3),
        45.applyDispersion(0.3),
        4000.applyDispersion(0.3),
        290.applyDispersion(0.3),
        200.applyDispersion(0.3),
        25.applyDispersion(0.3),
        783.applyDispersion(0.3))
    return rand
}