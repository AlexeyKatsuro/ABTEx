package com.e.btex.ui.widgets

import android.content.Context
import android.util.AttributeSet
import com.e.btex.data.SensorsType
import com.e.btex.data.entity.Sensors
import com.e.btex.data.entity.getSensorValue
import com.e.btex.data.entity.getStringWithUnits
import com.e.btex.util.UnixTimeUtils
import com.e.btex.util.extensions.defaultDatePattern
import com.e.btex.util.extensions.toFormattedStringUTC3
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SensorsPlot @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
) : LineChart(context, attr, defStyle) {

    private var referencePoint: Int = 0

    val lowestVisibleDate: Date
        get() = xAxisValueToDate(lowestVisibleX)

    val highestVisibleDate: Date
        get() = xAxisValueToDate(highestVisibleX)

    private var currentSensor: SensorsType = SensorsType.temperature

    private val dateFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val diffInMill = highestVisibleDate.time - lowestVisibleDate.time
            val pattern = when {

                countDate(diffInMill, TimeUnit.MINUTES) < 10 -> {
                    "HH:mm:ss"
                }
                countDate(diffInMill, TimeUnit.HOURS) < 1 -> {
                    "HH:mm"
                }
                countDate(diffInMill, TimeUnit.HOURS) < 5 -> {
                    "HH:mm"
                }
                countDate(diffInMill, TimeUnit.DAYS) < 1 -> {
                    "HH:mm"
                }
                countDate(diffInMill, TimeUnit.DAYS) < 30 -> {
                    "dd/MM"
                }
                countDate(diffInMill, TimeUnit.DAYS) >= 31 -> {
                    "dd/MM/yy"
                }
                else -> defaultDatePattern
            }

            return xAxisValueToDate(value).toFormattedStringUTC3(pattern)
        }
    }

    private val sensorValueFormatter = object : ValueFormatter() {
        val df = DecimalFormat("#.##")
        override fun getFormattedValue(value: Float): String {
            df.roundingMode = RoundingMode.CEILING
            return df.format(value)
        }
    }

    val sensorDataSet: SensorDataSet =
        SensorDataSet().apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueFormatter = sensorValueFormatter
            setDrawCircles(false)
        }

    init {
        // enable touch gestures
        setVisibleXRangeMaximum(100f)

        axisLeft.spaceBottom = 30f
        axisLeft.spaceTop = 30f

        axisRight.spaceBottom = 30f
        axisRight.spaceTop = 30f
        setTouchEnabled(true)
        description.isEnabled = false
        //enable scaling and dragging
        xAxis.granularity = 10f
        xAxis.valueFormatter = dateFormatter
        isDragEnabled = true
        isScaleXEnabled = true
        isScaleYEnabled = false
        legend.textSize  = 14f
        setMaxVisibleValueCount(15)
        // if disabled, scaling can be done on x- and y-axis separately
        // setPinchZoom(true)
    }


    fun setSensors(sensorsList: List<Sensors>) {
        //sensorDataSet.addAllEntry(sensorsList.map { SensorsEntry(it) })
        if(sensorsList.isNotEmpty()){
            referencePoint = sensorsList[0].timeSeconds
            sensorDataSet.zeroPoint = referencePoint
        }
        sensorDataSet.setSensors(sensorsList)
        updateChartDataSet()
    }

//    fun addSensor(sensors: Sensors){
//        if (data == null) LineData(sensorDataSet)
//        sensorDataSet.addSensors(sensors)
////        data.notifyDataChanged()
////        notifyDataSetChanged()
////        invalidate()
//    }

    fun setSensorsType(sensorsType: SensorsType) {
        currentSensor = sensorsType
        updateChartDataSet()
    }

    private fun updateChartDataSet() {
        sensorDataSet.label = currentSensor.getStringWithUnits(context)
        sensorDataSet.currentSensor = currentSensor
        sensorDataSet.update()
        sensorDataSet.notifyDataSetChanged()
        data = if (sensorDataSet.entryCount != 0) {
            LineData(sensorDataSet)
        } else {
            null
        }
        data?.notifyDataChanged()
        notifyDataSetChanged()
        invalidate()
    }

    fun addSensor(sensors: Sensors) {

        if (data == null) {
            data = LineData(sensorDataSet)
            referencePoint = sensors.timeSeconds
            sensorDataSet.zeroPoint = referencePoint
        }

        val set: SensorDataSet = data.getDataSetByIndex(0) as SensorDataSet

        set.addSensors(sensors)
        //set.addEntry(Entry((sensors.timeSeconds - referencePoint).toFloat(), sensors.getSensorValue(currentSensor)))

        data.notifyDataChanged()
        notifyDataSetChanged()
        invalidate()
        //moveViewToX(data.getEntryCount().toFloat())

        // this automatically refreshes the chart (calls invalidate())
        // chart.moveViewTo(data.getXValCount()-7, 55f,
        // AxisDependency.LEFT);
    }

    private fun xAxisValueToDate(value: Float): Date {
        return Date(TimeUnit.SECONDS.toMillis(referencePoint + value.toLong()))
    }

    private fun countDate(diffInMillies: Long, units: TimeUnit): Int {
        return units.convert(diffInMillies, TimeUnit.MILLISECONDS).toInt()
    }

    private fun LineDataSet.addAllEntry(list: List<Entry>){
        list.forEach {
            addEntry(it)
        }
    }
}


fun SensorDataSet.getLastEntry(): SensorsEntry{
    return getEntryForIndex(entryCount - 1) as SensorsEntry
}

fun SensorDataSet.isEmpty(): Boolean{
    return entryCount == 0
}

