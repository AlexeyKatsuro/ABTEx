package com.e.btex.data.repository

import androidx.lifecycle.LiveData
import com.e.btex.data.dao.SensorsDao
import com.e.btex.data.entity.Sensors
import com.e.btex.data.entity.getRandomValues
import com.e.btex.util.UnixTimeUtils
import com.e.btex.util.extensions.toFormattedStringUTC3
import kotlinx.coroutines.delay
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataBaseDataSource @Inject constructor(
    private val sensorsDao: SensorsDao
){
    fun getInTimeRangeLD(from: Long, to: Long): LiveData<List<Sensors>> =
        sensorsDao.getInTimeRangeLD(from,to)

    fun getAllSensorsLD(): LiveData<List<Sensors>> =
        sensorsDao.getAllSensorsLD()

    fun getLastIdLD(): LiveData<Int> =
        sensorsDao.getLastIdLD()

    fun getLastSensorLD(): LiveData<Sensors> =
        sensorsDao.getLastSensorLD()

    fun getAllSernsors(): List<Sensors> =
        sensorsDao.getAllSensors()

    fun getInTimeRange(from: Long, to: Long): List<Sensors> =
        sensorsDao.getInTimeRange(from,to)

    fun insertAll(sensorsList: List<Sensors>) =
        sensorsDao.insertAll(sensorsList)

    fun insert(sensors: Sensors) =
        sensorsDao.insert(sensors)

    fun wipe() =
        sensorsDao.wipe()

    fun getLastId(): Int? =
        sensorsDao.getLastId()

    suspend fun generateTestData(){
        val now = Date(UnixTimeUtils.currentUnixTimeMillis)
        val c = Calendar.getInstance()
        c.time = now
        c.add(Calendar.DATE, -2)
        var count = sensorsDao.getLastId()?: 0
        if(count==0) {
            while (c.time <= now) {
                Timber.e(c.time.toFormattedStringUTC3())
                c.add(Calendar.SECOND, 10)
                count++
                sensorsDao.insert(Sensors.getRandomValues(count, (c.timeInMillis / 1000).toInt()))
            }
        }

        while (true){
            delay(10000)
            sensorsDao.insert(Sensors.getRandomValues(++count,(UnixTimeUtils.currentUnixTimeSeconds)))
            Timber.e("count: $count")
        }

    }

}