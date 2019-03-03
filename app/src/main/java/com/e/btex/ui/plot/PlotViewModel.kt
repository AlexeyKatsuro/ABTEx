package com.e.btex.ui.plot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.e.btex.base.BaseViewModel
import com.e.btex.data.entity.Sensors
import com.e.btex.data.entity.getRandomValues
import com.e.btex.data.repository.SensorsRepository
import com.e.btex.util.extensions.switchMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlotViewModel @Inject constructor(
    private val sensorsRepository: SensorsRepository
) : BaseViewModel() {

    private val _lastSensors = MutableLiveData<Sensors>()
    val lastSensors: LiveData<Sensors>
        get() = _lastSensors


    private val loadDataTrigger = MutableLiveData<Unit>()
    val allSensors : LiveData<List<Sensors>> = loadDataTrigger.switchMap {
        sensorsRepository.getAllSernsorsLifeDate()
    }

    init {
       loadDataTrigger.value = Unit
    }
}