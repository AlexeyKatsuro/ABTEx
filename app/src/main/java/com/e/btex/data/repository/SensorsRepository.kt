package com.e.btex.data.repository

import androidx.lifecycle.LiveData
import com.e.btex.data.dao.SensorsDao
import com.e.btex.data.entity.Sensors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorsRepository @Inject constructor(
    private val sensorsDao: SensorsDao
) {

    fun getAllSernsorsLifeDate(): LiveData<List<Sensors>> = sensorsDao.getAllSernsorsLifeDate()

    suspend fun getAllSernsors(): List<Sensors> {
        return withContext(Dispatchers.IO) {
            sensorsDao.getAllSernsors()
        }
    }

    suspend fun getInTimeRange(from: Long, to: Long): List<Sensors> {
        return withContext(Dispatchers.IO) {
            sensorsDao.getInTimeRange(from, to)
        }
    }

    suspend fun insertAll(vararg sensors: Sensors) {
        withContext(Dispatchers.IO){
            sensorsDao.insertAll(*sensors)
        }
    }

    suspend fun wipe() {
        withContext(Dispatchers.IO) {
            sensorsDao.wipe()
        }
    }

    suspend fun getLastId(): Int {
        return withContext(Dispatchers.IO) {
            sensorsDao.getLastId()
        }
    }
}