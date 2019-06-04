package com.e.btex.data.repository

import androidx.lifecycle.LiveData
import com.e.btex.data.BtDevice
import com.e.btex.data.ServiceState
import com.e.btex.data.entity.Sensors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorsRepository @Inject constructor(
    private val dataBaseDataSource: DataBaseDataSource,
    private val bluetoothDataSource: BluetoothDataSource
) {

    fun getAllSensorsLD(): LiveData<List<Sensors>> = dataBaseDataSource.getAllSensorsLD()
    fun getLastSensorsLD(): LiveData<Sensors> = dataBaseDataSource.getLastSensorLD()
    fun getLastIdLD(): LiveData<Int> = dataBaseDataSource.getLastIdLD()

    suspend fun getAllSernsors(): List<Sensors> {
        return withContext(Dispatchers.IO) {
            dataBaseDataSource.getAllSernsors()
        }
    }

    suspend fun getInTimeRange(from: Long, to: Long): List<Sensors> {
        return withContext(Dispatchers.IO) {
            dataBaseDataSource.getInTimeRange(from, to)
        }
    }

    suspend fun insertAll(sensorsList: List<Sensors>) {
        withContext(Dispatchers.IO){
            dataBaseDataSource.insertAll(sensorsList)
        }
    }

    suspend fun resetLocaleStore() {
        withContext(Dispatchers.IO) {
            dataBaseDataSource.wipe()
        }
    }
    fun getDatabaseSize() : LiveData<Int> = dataBaseDataSource.getDatabaseSize()

    suspend fun getLastId(): Int? {
        return withContext(Dispatchers.IO) {
            dataBaseDataSource.getLastId()
        }
    }

    fun initConnection(device: BtDevice): LiveData<ServiceState> {
       return bluetoothDataSource.initConnection(device)
    }

    fun closeConnection() {
        bluetoothDataSource.closeConnection()
    }


    fun readLogs(fromId: Int, toId: Int) {
        bluetoothDataSource.readLogs(fromId, toId)
    }
    fun resetRemoteStorage() {
        bluetoothDataSource.resetStorage()
    }

    suspend fun generateTestData() {
       withContext(Dispatchers.IO){
           dataBaseDataSource.generateTestData()
       }
    }

    suspend fun getLastSensorsCount(count: Int): List<Sensors> {
       return withContext(Dispatchers.IO){
            dataBaseDataSource.getLastSensorsCount(count)
        }
    }
}