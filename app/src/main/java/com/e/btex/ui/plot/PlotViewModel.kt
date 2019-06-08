package com.e.btex.ui.plot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.e.btex.base.BaseViewModel
import com.e.btex.data.BtDevice
import com.e.btex.data.SensorsType
import com.e.btex.data.ServiceState
import com.e.btex.data.entity.LoadingData
import com.e.btex.data.entity.Sensors
import com.e.btex.data.repository.DeviceRepository
import com.e.btex.data.repository.SensorsRepository
import com.e.btex.util.Event
import com.e.btex.util.extensions.mapToEvent
import com.e.btex.util.extensions.switchMap
import com.e.btex.util.extensions.trigger
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

class PlotViewModel @Inject constructor(
    private val sensorsRepository: SensorsRepository,
    private val deviceRepository: DeviceRepository
) : BaseViewModel() {

    val lastSensors = sensorsRepository.getLastSensorsLD()

    val lastStoreId = sensorsRepository.getLastIdLD()

    private val loadDataTrigger = MutableLiveData<Unit>()
    val allSensors: LiveData<List<Sensors>> = loadDataTrigger.switchMap {
        sensorsRepository.getAllSensorsLD()
    }

    private val _loading = MediatorLiveData<LoadingData>()
    val loading: LiveData<LoadingData>
        get() = _loading

    private val _lastCount = MutableLiveData<List<Sensors>>()
    val lastCount: LiveData<List<Sensors>>
        get() = _lastCount

    private val loadDeviceTrigger = MutableLiveData<Unit>()
    val targetDevice: LiveData<Event<BtDevice?>> = loadDeviceTrigger.mapToEvent {
        deviceRepository.getTargetBtDevice()
    }
    private val connectionTrigger = MutableLiveData<BtDevice>()
    val connectionState: LiveData<ServiceState> =
        connectionTrigger.switchMap {
            sensorsRepository.initConnection(it)
        }

    private val _sensorsType = MutableLiveData<SensorsType>()
    val sensorsType: LiveData<SensorsType>
        get() = _sensorsType

    init {
        _sensorsType.value = SensorsType.temperature
        loadDataTrigger.trigger()
        _loading.addSource(connectionState) {
            if (it is ServiceState.OnReceiveData && it.data is LoadingData) {
                _loading.value = it.data
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

    fun generate() {
        scope.launch {
            sensorsRepository.generateTestData()
        }
    }

    fun getLastSensorsCount(count: Int) {
        load(call = { sensorsRepository.getLastSensorsCount(count) }) {
            _lastCount.value = it
            it.forEach {
                Timber.d("$it")
            }

        }
    }

    fun setSensorsType(type: SensorsType){
        _sensorsType.value = type
    }


}