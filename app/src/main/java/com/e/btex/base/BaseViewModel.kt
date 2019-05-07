package com.e.btex.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val job = Job()

    protected val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    protected open fun <T> load(
        call: suspend () -> T,
        onDataLoaded: (data: T) -> Unit
    ): Job {
        return scope.launch {
            onDataLoaded(call())
        }
    }
}