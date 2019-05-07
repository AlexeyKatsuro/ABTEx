package com.e.btex.ui.plot

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import com.e.btex.R
import com.e.btex.base.BaseFragment
import com.e.btex.data.ServiceState
import com.e.btex.data.entity.Sensors
import com.e.btex.databinding.FragmentPlotBinding
import com.e.btex.util.EventObserver
import com.e.btex.util.extensions.executeAfter
import com.e.btex.util.extensions.toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass


class PlotFragment : BaseFragment<FragmentPlotBinding, PlotViewModel>() {


    override val viewModelClass: KClass<PlotViewModel>
        get() = PlotViewModel::class
    override val layoutId: Int
        get() = R.layout.fragment_plot

    private val bleAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        binding.toolbar.apply {
            inflateMenu(R.menu.menu_main)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_device_settings -> {
                        navController.navigate(R.id.showSettingsFragment)
                        true
                    }
                    R.id.action_refresh -> {
                        viewModel.refreshConnection()
                        true
                    }
                    R.id.action_turn_off ->{
                        viewModel.closeConnection()
                        true
                    }
                    else -> false
                }
            }
        }

        binding.buttonRead.setOnClickListener {
            //viewModel.loadLastBunchData(1000)
            binding.appbarProgress.executeAfter {
                isVisible = !(isVisible?: false)
            }
        }

        setUpChart(binding.chart)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadTargetAddress()


        viewModel.targetDevice.observe(this, EventObserver {
            if (it == null) {

                navController.navigate(R.id.showSettingsFragment)
            } else {
                binding.executeAfter {
                    this.device = it
                }
                viewModel.initConnection(it)
            }
        })

        viewModel.connectionState.observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {

        })

        viewModel.lastSensors.observe(viewLifecycleOwner, Observer {
            binding.executeAfter {
                sensors = it
            }
        })

//        viewModel.allSensors.observe(viewLifecycleOwner, Observer {
//            it.forEach {
//                Timber.e("Id: ${it.id}")
//            }
//        })

    }

    private fun setUpChart(chart: LineChart) {
        chart.apply {
            // enable touch gestures
            setTouchEnabled(true)
            description.isEnabled = false
            // enable scaling and dragging
            // xAxis.granularity  = 1f
//            xAxis.valueFormatter = object : IAxisValueFormatter {
//                private val mFormat = SimpleDateFormat("HH:mm:ss", Locale.ROOT)
//                override fun getFormattedValue(value: Float, axis: AxisBase): String {
//                    val millis = TimeUnit.HOURS.toMillis(value.toLong())
//                    return value.toString()
//                    //return mFormat.format(Date(millis))
//                }
//
//            }
            setDragEnabled(true)
            setScaleEnabled(true)
            setDrawGridBackground(false)

            // if disabled, scaling can be done on x- and y-axis separately
            setPinchZoom(true)
        }
    }

    private fun addNewSensorVal(value: Float, time: Long) {

        var data: LineData? = binding.chart.getData()
        if (data == null) {
            data = LineData()
            binding.chart.setData(data)
        }

        var set: ILineDataSet? = data.getDataSetByIndex(0)
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet()
            data.addDataSet(set)
        }

        data.addEntry(Entry(time.toFloat(), value), 0)


        data.notifyDataChanged()
        binding.chart.notifyDataSetChanged()
        binding.chart.setVisibleXRangeMaximum(20f)
        binding.chart.moveViewToX(data.getEntryCount().toFloat())

        // this automatically refreshes the chart (calls invalidate())
        // chart.moveViewTo(data.getXValCount()-7, 55f,
        // AxisDependency.LEFT);
    }

    private fun createSet(): LineDataSet {

        val set = LineDataSet(null, "Dynamic Data")
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
//        set.axisDependency = YAxis.AxisDependency.LEFT
//        set.color = ColorTemplate.getHoloBlue()
//        set.lineWidth = 2f
//        set.circleRadius = 4f
//        set.fillAlpha = 65
//        set.fillColor = ColorTemplate.getHoloBlue()
//        set.highLightColor = Color.rgb(244, 117, 117)
//        set.valueTextColor = Color.WHITE
//        set.valueTextSize = 9f
        set.setDrawValues(false)
        return set
    }


    private fun updateUI(state: ServiceState) {
        updateToolbar(binding.toolbar, state)

        when (state) {
            is ServiceState.OnStartConnecting -> {
            }
            is ServiceState.OnFailedConnecting -> {
                toast(R.string.error_device_connection)
            }

            is ServiceState.OnCreateConnection -> {
                toast(R.string.state_connected)
            }

            is ServiceState.OnDestroyConnection -> {

            }
        }
    }

    private fun updateToolbar(toolbar: Toolbar, state: ServiceState) {
        toolbar.menu.findItem(R.id.action_refresh)?.isVisible =
            state is ServiceState.OnFailedConnecting || state is ServiceState.OnDestroyConnection

        when (state) {
            is ServiceState.OnStartConnecting -> {
                toolbar.subtitle = getString(R.string.state_connecting)
            }

            is ServiceState.OnDestroyConnection,
            is ServiceState.OnFailedConnecting -> {
                toolbar.subtitle = getString(R.string.state_offline)
            }

            is ServiceState.OnCreateConnection -> {
                toolbar.subtitle = getString(R.string.state_online)
            }
        }
    }


}