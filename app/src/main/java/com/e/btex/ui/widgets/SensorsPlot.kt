package com.e.btex.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.e.btex.data.SensorsType
import com.e.btex.data.entity.Sensors
import com.e.btex.data.entity.getSensorValue
import com.e.btex.data.getString
import com.e.btex.util.extensions.defaultDatePattern
import com.e.btex.util.extensions.toFormattedStringUTC3
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import timber.log.Timber
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

    private val dateFormatter = IAxisValueFormatter { value, axis ->
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

        xAxisValueToDate(value).toFormattedStringUTC3(pattern)
    }


    init {
        // enable touch gestures
        setVisibleXRangeMaximum(100f)
        axisLeft.spaceBottom = 20f
        axisLeft.spaceTop = 20f
        setTouchEnabled(true)
        description.isEnabled = false
        //enable scaling and dragging
        xAxis.granularity = 10f
        xAxis.valueFormatter = dateFormatter
        setDragEnabled(true)
        setDrawGridBackground(false)
        isScaleXEnabled = true
        isScaleYEnabled = false
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

    private fun updateChartDataSet(){
        referencePoint = if (currentDataSet.isNotEmpty()) currentDataSet[0].timeSeconds else 0
        val entry = currentDataSet.mapIndexed { index, sensors ->
            Entry((sensors.timeSeconds - referencePoint).toFloat(), sensors.getSensorValue(currentSensor))
        }
        val lineDataSet = LineDataSet(entry, currentSensor.getString(context))
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.setDrawCircles(false)
        data = LineData(lineDataSet)
        invalidate()
    }

    private fun xAxisValueToDate(value: Float): Date {
        return Date(TimeUnit.SECONDS.toMillis(referencePoint + value.toLong()))
    }

    private fun countDate(diffInMillies: Long, units: TimeUnit): Int {
        return units.convert(diffInMillies, TimeUnit.MILLISECONDS).toInt()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_MOVE){
            Timber.e("scaleX $scaleX")
        }
        return super.onTouchEvent(event)
    }
}

