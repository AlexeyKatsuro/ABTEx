package com.e.btex.ui.plot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.e.btex.base.BaseViewModel
import com.e.btex.data.BtDevice
import com.e.btex.data.ServiceState
import com.e.btex.data.entity.Sensors
import com.e.btex.data.entity.StatusData
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


    val lastSensors = sensorsRepository.getLastSensorsLD()

    private val _status = MediatorLiveData<StatusData>()
    val status: LiveData<StatusData>
        get() = _status

    private val loadDeviceTrigger = MutableLiveData<Unit>()
    val targetDevice: LiveData<Event<BtDevice?>> = loadDeviceTrigger.mapToEvent {
        deviceRepository.getTargetBtDevice()
    }
    private val connectionTrigger = MutableLiveData<BtDevice>()
    val connectionState: LiveData<ServiceState> =
        connectionTrigger.switchMap {
            sensorsRepository.initConnection(it)
        }

    init {
        _status.addSource(connectionState) {
            if (it is ServiceState.OnReceiveData && it.data is StatusData) {
                _status.value = it.data
            }
        }
    }

    fun loadTargetAddress() {
        loadDeviceTrigger.trigger()
    }

    fun initConnection(device: BtDevice) {
        connectionTrigger.value = device
    }

    fun refreshConnection() {
        val device = deviceRepository.getTargetBtDevice()
        device?.let {
            initConnection(it)
        }
    }

    fun closeConnection() {
        sensorsRepository.closeConnection()
    }

    fun readLogs(fromId: Int, toId: Int) {
        sensorsRepository.readLogs(fromId, toId)
    }

    fun loadLastBunchData(count: Int) {
        status.value?.let {
            val start = (it.lastLogId - count).coerceAtLeast(1)
            sensorsRepository.readLogs(start, it.lastLogId)
        }
    }

}