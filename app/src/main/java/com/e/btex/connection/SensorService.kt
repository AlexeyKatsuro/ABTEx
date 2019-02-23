package com.e.btex.connection

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.e.btex.data.AppDataBase
import com.e.btex.data.entity.Sensors
import com.e.btex.data.entity.getRandomValues
import com.e.btex.data.repository.SensorsRepository
import com.e.btex.util.extensions.switchMap
import dagger.android.AndroidInjection
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class SensorService : LifecycleService() {
    private val job = Job()

    protected val scope = CoroutineScope(Dispatchers.Main + job)


    @Inject
    lateinit var sensorRepository: SensorsRepository

    private val loadSensorTrigger = MutableLiveData<Unit>()
    val allSensorsData: LiveData<List<Sensors>> = loadSensorTrigger.switchMap {
        sensorRepository.getAllSernsors()
    }


    companion object {
        private var isRunning: Boolean = false
        fun startService(context: Context) {
            if (!isRunning) {
                val intent = Intent(context, SensorService::class.java)
                ContextCompat.startForegroundService(context, intent)
            }
        }


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand")
        loadSensorTrigger.value = Unit

        allSensorsData.observe(this, Observer {
            Timber.e("$it")
        })

        addSensors(Sensors.getRandomValues(0, System.currentTimeMillis()))

        scope.launch(Dispatchers.IO) {
            delay(10000)
            stopSelf()
            wipe()
        }

        scope.launch(Dispatchers.IO) {

        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun addSensors(sensors: Sensors){
        scope.launch(Dispatchers.IO) {
            sensorRepository.insertAll(sensors)
        }
    }

    fun wipe(){
        scope.launch(Dispatchers.IO) {
            sensorRepository.wipe()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
        isRunning = false
        job.cancel()
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
        Timber.d("onCreate")
        isRunning = true

    }
}