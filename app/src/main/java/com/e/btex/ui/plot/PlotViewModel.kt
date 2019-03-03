package com.e.btex.ui.plot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.e.btex.base.BaseViewModel
import com.e.btex.data.BtDevice
import com.e.btex.data.entity.Sensors
import com.e.btex.data.repository.DeviceRepository
import com.e.btex.data.repository.SensorsRepository
import com.e.btex.util.Event
import com.e.btex.util.extensions.mapToEvent
import com.e.btex.util.extensions.switchMap
import com.e.btex.util.extensions.trigger
import javax.inject.Inject

class PlotViewModel @Inject constructor(
    private val sensorsRepository: SensorsRepository,
    private val deviceRepository: DeviceRepository
) : BaseViewModel() {

    private val _lastSensors = MutableLiveData<Sensors>()
    val lastSensors: LiveData<Sensors>
        get() = _lastSensors


    private val loadDataTrigger = MutableLiveData<Unit>()
    val allSensors : LiveData<List<Sensors>> = loadDataTrigger.switchMap {
        sensorsRepository.getAllSensorsLiveDate()
    }

    private val loadAderssTrigger = MutableLiveData<Unit>()
    val targetAddress: LiveData<Event<String?>> = loadAderssTrigger.mapToEvent {
        deviceRepository.getTargetAddress()
    }

    init {
       loadDataTrigger.trigger()

    }

    fun loadTargetAddress(){
        loadAderssTrigger.trigger()
    }

    fun initConnection(device: BtDevice) {
       sensorsRepository.initConnection(device)
    }

}