package com.e.btex.data.mappers

import com.e.btex.data.entity.Sensors
import com.e.btex.data.entity.StatusData
import com.e.btex.util.UnixTimeUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatusDataMapper @Inject constructor() : Mapper<StatusData, Sensors> {
    override fun map(from: StatusData): Sensors {
        return Sensors(
            id = from.lastLogId,
            timeSeconds = UnixTimeUtils.currentUnixTimeSeconds,
            temperature = from.sensors.temperature.toFloat()/100,
            humidity = from.sensors.humidity.toFloat()/100,
            co2 = from.sensors.co2.toFloat(),
            pm1 = from.sensors.pm1.toFloat(),
            pm10 = from.sensors.pm10.toFloat(),
            pm25 = from.sensors.pm25.toFloat(),
            tvoc = from.sensors.tvoc.toFloat()
        )
    }

}