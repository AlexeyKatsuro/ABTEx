package com.e.btex.ui.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE
import android.bluetooth.BluetoothAdapter.getDefaultAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.afollestad.recyclical.datasource.DataSource
import com.afollestad.recyclical.datasource.emptyDataSourceTyped
import com.afollestad.recyclical.setup
import com.e.btex.R
import com.e.btex.base.BaseFragment
import com.e.btex.data.BtDevice
import com.e.btex.databinding.FragmentSettingsBinding
import com.e.btex.databinding.ItemBluetoothDeciveBinding
import com.e.btex.util.AutoSubscribeReceiver
import com.e.btex.util.broadcastReceivers.BluetoothStateReceiver
import com.e.btex.util.extensions.hideKeyboard
import com.e.btex.util.extensions.includeItem
import com.e.btex.util.extensions.onDataBindingBind
import com.e.btex.util.extensions.toast
import kotlin.reflect.KClass


class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {


    private val args by navArgs<SettingsFragmentArgs>()

    override val viewModelClass: KClass<SettingsViewModel>
        get() = SettingsViewModel::class
    override val layoutId: Int
        get() = R.layout.fragment_settings

    private var onStateChangedReceiver
        by AutoSubscribeReceiver<BluetoothStateReceiver>()

    private val bleAdapter: BluetoothAdapter = getDefaultAdapter()

    private val dataSourceTyped: DataSource<BtDevice> = emptyDataSourceTyped()


    private val isBtEnable: Boolean
        get() = bleAdapter.isEnabled

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onStateChangedReceiver = BluetoothStateReceiver()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        if (args.isStart) {
            binding.includeAppBar.toolBar.navigationIcon = null
        } else {
            binding.includeAppBar.toolBar.setNavigationOnClickListener {
                hideKeyboard()
                navController.navigateUp()
                navController.currentDestination?.parent
            }
        }

        binding.switchContainer.setOnClickListener {
            if (isBtEnable) {
                bleAdapter.disable()
            } else {
                showBluetoothEnableDialog()
            }
        }


        binding.pairedDeviceRecyclerView.setup {
            withDataSource(dataSourceTyped)
            includeItem<BtDevice>(R.layout.item_bluetooth_decive) {

                onDataBindingBind(ItemBluetoothDeciveBinding::bind) { _, item ->
                    itemBinding.isTarget = viewModel.targetDevice.value?.macAddress == item.macAddress
                }

                onClick {
                    viewModel.setTargetDevice(item)
                    val directions = SettingsFragmentDirections.showPlotFragment()
                    navController.navigate(directions)
                }

            }
        }
        updatePairedDevices()
        binding.bluetoothSwitch.isChecked = isBtEnable

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.targetDevice.observe(this, Observer {
            if (it == null) {
                toast("Выберите устройство")
            }
        })

        onStateChangedReceiver.setOnStateChangedListener(object : BluetoothStateReceiver.OnStateChangedListener {
            override fun onStateOff() {
                binding.bluetoothSwitch.isChecked = false
                updatePairedDevices()
            }

            override fun onStateOn() {
                binding.bluetoothSwitch.isChecked = true
                updatePairedDevices()
            }

            override fun onStateTurningOff() = Unit

            override fun onStateTurningOn() = Unit

        })
    }

    private fun showBluetoothEnableDialog() {
        val enableBtIntent = Intent(ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, 0)
    }

    private fun updatePairedDevices() {
        val devices = bleAdapter.bondedDevices.map { BtDevice(it.name, it.address) }
        dataSourceTyped.set(devices)
    }
}