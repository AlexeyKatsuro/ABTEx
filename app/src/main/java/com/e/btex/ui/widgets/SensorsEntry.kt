package com.e.btex.ui.widgets

import com.e.btex.data.SensorsType
import com.e.btex.data.entity.Sensors
import com.e.btex.data.entity.getSensorValue
import com.github.mikephil.charting.data.Entry

class SensorsEntry(val sensors: Sensors) : Entry() {
    var zeroPoint: Int = 0
    var currentSensor = SensorsType.temperature

    init {
        data = sensors
    }

    override fun getY(): Float {
        return sensors.getSensorValue(currentSensor)
    }


    override fun getX(): Float {
        return (sensors.timeSeconds - zeroPoint).toFloat()
    }
}