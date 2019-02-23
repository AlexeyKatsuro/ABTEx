package com.e.btex.connection

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import kotlinx.coroutines.*
import timber.log.Timber

class SensorService : LifecycleService() {
    private val job = Job()

    protected val scope = CoroutineScope(Dispatchers.Main + job)

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
        var count = 0
        scope.launch(Dispatchers.IO) {
            while (true) {
                delay(1000)
                Timber.e("$count")
                if (count++ == 20) {
                    break
                }
            }
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
        isRunning = false
        job.cancel()
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        isRunning = true

    }
}