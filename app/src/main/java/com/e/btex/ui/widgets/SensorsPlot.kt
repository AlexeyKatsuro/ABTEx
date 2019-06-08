package com.e.btex.ui.widgets

import android.content.Context
import android.util.AttributeSet
import com.e.btex.data.SensorsType
import com.e.btex.data.entity.Sensors
import com.e.btex.data.entity.getSensorValue
import com.e.btex.data.entity.getStringWithUnits
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
    private var currentDataSet: List<Sensors> = emptyList()

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
        // if disabled, scaling can be done on x- and y-axis separately
        // setPinchZoom(true)
    }


    fun setSensors(sensorsList: List<Sensors>) {
        currentDataSet = sensorsList
        updateChartDataSet()
    }

    fun setSensorsType(sensorsType: SensorsType) {
        currentSensor = sensorsType
        updateChartDataSet()
    }

    private fun updateChartDataSet() {
        referencePoint = if (currentDataSet.isNotEmpty()) currentDataSet[0].timeSeconds else 0
        val entry = currentDataSet.mapIndexed { index, sensors ->
            Entry((sensors.timeSeconds - referencePoint).toFloat(), sensors.getSensorValue(currentSensor))
        }
        val lineDataSet = LineDataSet(entry, currentSensor.getStringWithUnits(context))
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.valueFormatter = sensorValueFormatter
        lineDataSet.setDrawCircles(false)
        data = if (lineDataSet.entryCount != 0) {
            LineData(lineDataSet)
        } else {
            null
        }
        setMaxVisibleValueCount(15)
        invalidate()
    }

    fun addSensor(sensors: Sensors) {

        if (data == null) {
            data = LineData(LineDataSet(null, ""))
        }

        val set: ILineDataSet = data.getDataSetByIndex(0)
        set.addEntry(Entry((sensors.timeSeconds - referencePoint).toFloat(), sensors.getSensorValue(currentSensor)))


        data.notifyDataChanged()
        notifyDataSetChanged()
        moveViewToX(data.getEntryCount().toFloat())

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

}

