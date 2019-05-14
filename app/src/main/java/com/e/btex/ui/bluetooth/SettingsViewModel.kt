package com.e.btex.ui.bluetooth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.e.btex.base.BaseViewModel
import com.e.btex.data.repository.DeviceRepository
import com.e.btex.util.extensions.map
import com.e.btex.util.extensions.trigger
import javax.inject.Inject


class SettingsViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : BaseViewModel() {


    private val loadAdderssTrigger = MutableLiveData<Unit>()
    val targetAddress: LiveData<String?> = loadAdderssTrigger.map {
        deviceRepository.getTargetAddress()
    }


    init {
        loadAdderssTrigger.trigger()
    }

    fun setTargetAddress(deviceAddress: String){
        deviceRepository.setTargetAddress(deviceAddress)
        loadAdderssTrigger.trigger()
    }
}