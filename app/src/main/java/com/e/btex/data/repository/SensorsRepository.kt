package com.e.btex.data.repository

import androidx.lifecycle.LiveData
import com.e.btex.data.dao.SensorsDao
import com.e.btex.data.entity.Sensors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorsRepository @Inject constructor(
        private val sensorsDao: SensorsDao
) {
    fun getAllSernsors(): LiveData<List<Sensors>> = sensorsDao.getAllSernsors()

    fun getInTimeRange(from: Long, to: Long): LiveData<List<Sensors>> = sensorsDao.getInTimeRange(from, to)

    fun insertAll(vararg sensors: Sensors) = sensorsDao.insertAll(*sensors)

    fun wipe() = sensorsDao.wipe()

    fun getLastId(): Int = sensorsDao.getLastId()
}