package com.e.btex.ui.plot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.e.btex.base.BaseViewModel
import com.e.btex.util.Event
import com.e.btex.util.extensions.map
import javax.inject.Inject

class PlotViewModel @Inject constructor() : BaseViewModel(){

    private val _onStart = MutableLiveData<Unit>()
    val onStart: LiveData<Event<Unit>> = _onStart.map { Event(Unit) }

    init {
        _onStart.value = Unit
    }
}