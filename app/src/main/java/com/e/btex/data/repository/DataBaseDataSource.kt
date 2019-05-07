package com.e.btex.data.repository

import androidx.lifecycle.LiveData
import com.e.btex.data.dao.SensorsDao
import com.e.btex.data.entity.Sensors
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

    fun getLastId(): Int =
        sensorsDao.getLastId()

}