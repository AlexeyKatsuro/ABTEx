package com.e.btex.ui.bluetooth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.e.btex.base.BaseViewModel
import com.e.btex.data.BtDevice
import com.e.btex.data.repository.DeviceRepository
import com.e.btex.util.extensions.map
import com.e.btex.util.extensions.trigger
import javax.inject.Inject


class SettingsViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : BaseViewModel() {


    private val loadTargetDevice = MutableLiveData<Unit>()
    val targetDevice: LiveData<BtDevice?> = loadTargetDevice.map {
        deviceRepository.getTargetBtDevice()
    }


    init {
        loadTargetDevice.trigger()
    }


    fun setTargetDevice(device: BtDevice) {
        deviceRepository.setTargetDevice(device)
        loadTargetDevice.trigger()
    }
}