package com.e.btex.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.e.btex.R
import com.e.btex.data.SensorsType
import com.e.btex.data.entity.Sensors
import com.e.btex.data.entity.getStringWithUnits
import com.e.btex.util.extensions.defaultDatePattern
import com.e.btex.util.extensions.toFormattedStringUTC3
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
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
    private var currentSensor2: SensorsType = SensorsType.co2

    val gestureListener = object : OnChartGestureListener {
        override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) = Unit


        override fun onChartSingleTapped(me: MotionEvent?) = Unit

        override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) =
            Unit

        override fun onChartLongPressed(me: MotionEvent?) = Unit

        override fun onChartDoubleTapped(me: MotionEvent?) = Unit

        override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
            updateDescription()
        }

        override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
            updateDescription()
        }

        override fun onChartFling(me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float) {
            updateDescription()
        }

    }

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
            setAxisDependency(YAxis.AxisDependency.LEFT)
            color = ColorTemplate.getHoloBlue()
            valueFormatter = sensorValueFormatter
            setDrawCircles(false)
        }
    val sensorDataSet2: SensorDataSet = SensorDataSet().apply {
        mode = LineDataSet.Mode.CUBIC_BEZIER
        setAxisDependency(YAxis.AxisDependency.RIGHT)
        color = (ContextCompat.getColor(context, R.color.colorPrimary))
        valueFormatter = sensorValueFormatter
        setDrawCircles(false)
    }

    init {
        // enable touch gestures

        axisLeft.spaceBottom = 30f
        axisLeft.spaceTop = 30f
        axisLeft.setTextColor(ColorTemplate.getHoloBlue())

        axisRight.spaceBottom = 30f
        axisRight.spaceTop = 30f
        axisRight.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        setTouchEnabled(true)
        description.isEnabled = false
        xAxis.granularity = 10f
        xAxis.spaceMin = 10f
        xAxis.spaceMax = 10f
        xAxis.valueFormatter = dateFormatter
        isDragEnabled = true
        isScaleXEnabled = true
        isScaleYEnabled = false
        legend.textSize = 14f
        setMaxVisibleValueCount(15)
        isHighlightPerDragEnabled = false
        isHighlightPerDragEnabled = false
        onChartGestureListener = gestureListener
    }


    fun setSensors(sensorsList: List<Sensors>) {
        //sensorDataSet.addAllEntry(sensorsList.map { SensorsEntry(it) })
        if (sensorsList.isNotEmpty()) {
            referencePoint = sensorsList[0].timeSeconds
            sensorDataSet.zeroPoint = referencePoint
            sensorDataSet2.zeroPoint = referencePoint
        }
        sensorDataSet.setSensors(sensorsList)
        sensorDataSet2.setSensors(sensorsList)
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

    fun setSensorsType2(sensorsType: SensorsType) {
        currentSensor2 = sensorsType
        updateChartDataSet()
    }

    private fun updateChartDataSet() {
        sensorDataSet.label = currentSensor.getStringWithUnits(context)
        sensorDataSet2.label = currentSensor2.getStringWithUnits(context)
        sensorDataSet.currentSensor = currentSensor
        sensorDataSet2.currentSensor = currentSensor2
        sensorDataSet.update()
        sensorDataSet2.update()
        sensorDataSet.notifyDataSetChanged()
        sensorDataSet2.notifyDataSetChanged()
        data = if (sensorDataSet.entryCount != 0 || sensorDataSet2.entryCount != 0) {
            LineData(sensorDataSet, sensorDataSet2)
        } else {
            null
        }
        data?.notifyDataChanged()
        notifyDataSetChanged()
        updateDescription()
        invalidate()
    }

    fun addSensor(sensors: Sensors) {

        if (data == null) {
            data = LineData(sensorDataSet, sensorDataSet2)
            referencePoint = sensors.timeSeconds
            sensorDataSet.zeroPoint = referencePoint
            sensorDataSet2.zeroPoint = referencePoint
        }

        val set: SensorDataSet = data.getDataSetByIndex(0) as SensorDataSet
        val set2: SensorDataSet = data.getDataSetByIndex(1) as SensorDataSet

        set.addSensors(sensors)
        set2.addSensors(sensors)
        //set.addEntry(Entry((sensors.timeSeconds - referencePoint).toFloat(), sensors.getSensorValue(currentSensor)))

        data.notifyDataChanged()
        notifyDataSetChanged()
        updateDescription()
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

    private fun LineDataSet.addAllEntry(list: List<Entry>) {
        list.forEach {
            addEntry(it)
        }
    }


    private fun updateDescription() {
        val diffInMill = highestVisibleDate.time - lowestVisibleDate.time
        val pattern = "dd/MM/yy"
        val text = when {

            countDate(diffInMill, TimeUnit.DAYS) < 1 -> {
                lowestVisibleDate.toFormattedStringUTC3(pattern)
            }

            else -> "${lowestVisibleDate.toFormattedStringUTC3(pattern)} " +
                "- ${highestVisibleDate.toFormattedStringUTC3(pattern)}"
        }

        val descr = description
        descr.isEnabled = true
        descr.text = text
        descr.textSize = 14f
    }

}


fun SensorDataSet.getLastEntry(): SensorsEntry {
    return getEntryForIndex(entryCount - 1) as SensorsEntry
}

fun SensorDataSet.isEmpty(): Boolean {
    return entryCount == 0
}

