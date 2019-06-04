package com.e.btex.ui.datebase

import com.e.btex.base.BaseViewModel
import com.e.btex.data.ServiceStates
import com.e.btex.data.repository.DeviceRepository
import com.e.btex.data.repository.SensorsRepository
import com.e.btex.util.extensions.map
import com.e.btex.util.extensions.switchMap
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseInfoViewModel @Inject constructor(
    private val sensorsRepository: SensorsRepository,
    private val deviceRepository: DeviceRepository
) : BaseViewModel() {

    val dataBaseSize = sensorsRepository.getDatabaseSize()
    val lastSensor = sensorsRepository.getLastSensorsLD()
    val isServiceOnline = deviceRepository.getServiceState().map {
        it == ServiceStates.CreateConnection || it == ServiceStates.OnReceiveData
    }

    fun resetLocaleStore(){
        scope.launch {
            sensorsRepository.resetLocaleStore()
        }
    }

    fun resetRemoteStore(){
       sensorsRepository.resetRemoteStorage()
    }

}