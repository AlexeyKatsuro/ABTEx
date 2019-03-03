package com.e.btex.ui.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE
import android.bluetooth.BluetoothAdapter.getDefaultAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.btex.R
import com.e.btex.base.BaseFragment
import com.e.btex.connection.BleService
import com.e.btex.connection.ServiceStateCallback
import com.e.btex.data.BtDevice
import com.e.btex.databinding.FragmentSettingsBinding
import com.e.btex.databinding.ItemBluettoothDeciveBinding
import com.e.btex.ui.adapters.DataBoundAdapter
import com.e.btex.util.AutoSubscribeReceiver
import com.e.btex.util.broadcastReceivers.BluetoothStateReceiver
import com.e.btex.util.extensions.act
import com.e.btex.util.extensions.hideKeyboard
import com.e.btex.util.extensions.longToast
import com.e.btex.util.extensions.toast
import timber.log.Timber
import kotlin.reflect.KClass


class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {



    override val viewModelClass: KClass<SettingsViewModel>
        get() = SettingsViewModel::class
    override val layoutId: Int
        get() = R.layout.fragment_settings

    private var onStateChangedReceiver
        by AutoSubscribeReceiver<BluetoothStateReceiver>()

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var pairDeviceAdapter: DataBoundAdapter<BtDevice,ItemBluettoothDeciveBinding>

    private val isBtEnable: Boolean
        get() = bluetoothAdapter.isEnabled

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onStateChangedReceiver = BluetoothStateReceiver()
        setUpBluetoothAdapter()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.includeAppBar.toolBar.setNavigationOnClickListener {
            hideKeyboard()
            navController.navigateUp()
        }

        binding.switchContainer.setOnClickListener {
            if (isBtEnable) {
                bluetoothAdapter.disable()
            } else {
                showBluetoothEnableDialog()
            }
        }

        pairDeviceAdapter  = DataBoundAdapter(R.layout.item_bluettooth_decive, BtDevice::macAddress)
        pairDeviceAdapter.setOnItemClickListener { _, _, item ->
          //  BleService.startService(act.applicationContext,item)
        }


        binding.pairedDeviceRecyclerView.adapter = pairDeviceAdapter
        updatePairedDevices()
        binding.bluetoothSwitch.isChecked  = isBtEnable

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onStateChangedReceiver.setOnStateChangedListener(object : BluetoothStateReceiver.OnStateChangedListener {
            override fun onStateOff() {
                binding.bluetoothSwitch.isChecked = false
                updatePairedDevices()
            }

            override fun onStateOn() {
                binding.bluetoothSwitch.isChecked = true
                updatePairedDevices()
            }

            override fun onStateTurningOff()  = Unit

            override fun onStateTurningOn() = Unit

        })
    }

    private fun showBluetoothEnableDialog() {
        val enableBtIntent = Intent(ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, 0)
    }


    private fun setUpBluetoothAdapter() {
        val adapter: BluetoothAdapter? = getDefaultAdapter()
        if (adapter != null) {
            bluetoothAdapter = adapter
        } else {
            longToast(R.string.error_bluetooth_not_supported)
            act.finish()

        }
    }

    private fun updatePairedDevices(){
        pairDeviceAdapter.submitList(bluetoothAdapter.bondedDevices.map { BtDevice(it.name,it.address) })
    }
}