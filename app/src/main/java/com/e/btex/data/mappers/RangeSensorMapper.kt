package com.e.btex.data.mappers

import com.e.btex.data.entity.ArrayLogData
import com.e.btex.data.entity.Sensors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RangeSensorMapper @Inject constructor() : Mapper<ArrayLogData, List<Sensors>> {
    override fun map(from: ArrayLogData): List<Sensors> {
        return from.logList.mapIndexed { index, logRowData ->
            Sensors(
                id = from.formId + index,
                timeSeconds = logRowData.rTime,
                temperature = logRowData.sensorsData.temperature.toFloat() / 100,
                humidity = logRowData.sensorsData.humidity.toFloat() / 100,
                co2 = logRowData.sensorsData.co2.toFloat(),
                pm1 = logRowData.sensorsData.pm1.toFloat(),
                pm10 = logRowData.sensorsData.pm10.toFloat(),
                pm25 = logRowData.sensorsData.pm25.toFloat(),
                tvoc = logRowData.sensorsData.tvoc.toFloat()
            )
        }
    }

}