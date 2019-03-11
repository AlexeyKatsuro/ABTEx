package com.e.btex.data.mappers

import com.e.btex.data.entity.Sensors
import com.e.btex.data.entity.SensorsData
import com.e.btex.util.UnixTimeUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorsMapper @Inject constructor() : Mapper<SensorsData, Sensors> {
    override fun map(from: SensorsData): Sensors {
        return Sensors(
            time = UnixTimeUtils.currentUnixTime.toLong(),
            temperature = from.temperature.toFloat()/100,
            humidity = from.humidity.toFloat()/100,
            co2 = from.co2.toFloat(),
            pm1 = from.pm1.toFloat(),
            pm10 = from.pm10.toFloat(),
            pm25 = from.pm25.toFloat(),
            tvoc = from.tvoc.toFloat()
        )
    }

}