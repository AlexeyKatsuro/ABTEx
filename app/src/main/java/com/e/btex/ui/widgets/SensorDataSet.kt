package com.e.btex.ui.widgets

import com.e.btex.data.SensorsType
import com.e.btex.data.entity.Sensors
import com.github.mikephil.charting.data.LineDataSet

class SensorDataSet : LineDataSet(null,""){
    var zeroPoint: Int = 0
    var currentSensor = SensorsType.temperature



    fun addSensors(sensors: Sensors){
        addEntry(SensorsEntry(sensors).updateState())
    }

    fun setSensors(list: List<Sensors>){
        values = list.map { SensorsEntry(it).updateState() }
    }


    private fun SensorsEntry.updateState(): SensorsEntry{
        zeroPoint = this@SensorDataSet.zeroPoint
        currentSensor = this@SensorDataSet.currentSensor
        return this
    }

    fun update() {
        (values as List<SensorsEntry>).forEach{it.updateState()}
    }
}