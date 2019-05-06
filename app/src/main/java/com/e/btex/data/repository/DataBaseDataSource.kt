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
    fun getInTimeRangeLifeDate(from: Long, to: Long): LiveData<List<Sensors>> =
        sensorsDao.getInTimeRangeLifeDate(from,to)

    fun getAllSensorsLiveDate(): LiveData<List<Sensors>> =
        sensorsDao.getAllSensorsLifeDate()

    fun getAllSernsors(): List<Sensors> =
        sensorsDao.getAllSensors()

    fun getInTimeRange(from: Long, to: Long): List<Sensors> =
        sensorsDao.getInTimeRange(from,to)

    fun insertAll(vararg sensors: Sensors) =
        sensorsDao.insertAll(*sensors)

    fun wipe() =
        sensorsDao.wipe()

    fun getLastId(): Int =
        sensorsDao.getLastId()

}