package com.e.btex.ui.plot

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.e.btex.R
import com.e.btex.base.BaseFragment
import com.e.btex.data.SensorsType
import com.e.btex.data.ServiceState
import com.e.btex.databinding.FragmentPlotBinding
import com.e.btex.databinding.NavHeaderMainBinding
import com.e.btex.ui.MainActivity
import com.e.btex.util.EventObserver
import com.e.btex.util.extensions.executeAfter
import com.e.btex.util.extensions.observeNotNull
import com.e.btex.util.extensions.observeValue
import com.e.btex.util.extensions.toast
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlin.reflect.KClass


class PlotFragment : BaseFragment<FragmentPlotBinding, PlotViewModel>() {


    override val viewModelClass: KClass<PlotViewModel>
        get() = PlotViewModel::class
    override val layoutId: Int
        get() = R.layout.fragment_plot

    private val bleAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private val navHeader: NavHeaderMainBinding
        get() = (requireActivity() as MainActivity).header

    private val drawerLayout: DrawerLayout
        get() = (requireActivity() as MainActivity).drawerLayout

    private lateinit var sensorViewMap: Map<SensorsType, View>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        sensorViewMap = getSensorViewMap()

        binding.toolbar.apply {
            inflateMenu(R.menu.menu_actions)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_refresh -> {
                        viewModel.refreshConnection()
                        true
                    }
                    R.id.action_turn_off -> {
                        viewModel.closeConnection()
                        true
                    }
                    else -> false
                }
            }
        }
        navHeader.includeSensors.apply {
            sensorViewMap.forEach { entry ->
                entry.value.setOnClickListener {
                    viewModel.setSensorsType(entry.key)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadTargetAddress()
        //viewModel.getLastSensorsCount(1000)

        viewModel.targetDevice.observe(this, EventObserver {
            if (it == null) {
                val directions = PlotFragmentDirections.startWithSettingsFragment()
                directions.isStart = true
                navController.navigate(directions)
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

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            binding.appbarProgress.executeAfter {
                isVisible = it.isLoading
                progress = it.progress
                size = it.size
            }
        })

        viewModel.lastSensors.observeNotNull(viewLifecycleOwner) {

            binding.chart.addSensor(it)

            //viewModel.getLastSensorsCount(1000)
            (requireActivity() as MainActivity).header.executeAfter {
                sensors = it
            }
        }

//        viewModel.lastCount.observeValue(viewLifecycleOwner) {
//
//            if (it.isNotEmpty()) {
//                binding.chart.setSensors(it)
//            }
//        }

        viewModel.sensorsType.observe(viewLifecycleOwner, Observer {
            binding.chart.setSensorsType(it)
            sensorViewMap.forEach {entry ->
                entry.value.isActivated = entry.key == it
            }
        })

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
                toolbar.menu.findItem(R.id.action_turn_off).isVisible = false
            }
            is ServiceState.OnReceiveData,
            is ServiceState.OnCreateConnection -> {
                toolbar.subtitle = getString(R.string.state_online)
                toolbar.menu.findItem(R.id.action_turn_off).isVisible = true
            }
        }
    }

    private fun getSensorViewMap(): Map<SensorsType, View> {
        return mapOf(
            SensorsType.temperature to navHeader.includeSensors.rowTemperature,
            SensorsType.humidity to navHeader.includeSensors.rowHumidity,
            SensorsType.co2 to navHeader.includeSensors.rowCo2,
            SensorsType.pm1 to navHeader.includeSensors.rowPm1,
            SensorsType.pm25 to navHeader.includeSensors.rowPm25,
            SensorsType.pm10 to navHeader.includeSensors.rowPm10,
            SensorsType.tvoc to navHeader.includeSensors.rowTvoc
        )
    }

}