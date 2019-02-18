package com.e.btex.ui.plot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.e.btex.base.BaseViewModel
import com.e.btex.data.Sensors
import com.e.btex.data.getRandomValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlotViewModel @Inject constructor() : BaseViewModel() {

    private val _lastSensors = MutableLiveData<Sensors>()
    val lastSensors: LiveData<Sensors>
        get() = _lastSensors

    init {
        scope.launch {
            withContext(Dispatchers.IO) {
                while (true) {
                    _lastSensors.postValue(Sensors.getRandomValues())
                    delay(3000)
                }

            }
        }
    }
}