package com.e.btex.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.e.btex.data.entity.Sensors

@Dao
interface SensorsDao {


    @Query("SELECT * FROM Sensors WHERE timeSeconds >= :from AND timeSeconds < :to")
    fun getInTimeRangeLD(from: Long, to: Long): LiveData<List<Sensors>>

    @Query("SELECT * FROM sensors")
    fun getAllSensorsLD(): LiveData<List<Sensors>>

    @Query("SELECT MAX(id) FROM sensors")
    fun getLastIdLD(): LiveData<Int>

    @Query("SELECT * FROM sensors WHERE id = (SELECT MAX(id) FROM sensors)")
    fun getLastSensorLD(): LiveData<Sensors>

    @Query("SELECT * FROM sensors")
    fun getAllSensors(): List<Sensors>

    @Query("SELECT * FROM Sensors WHERE timeSeconds >= :from AND timeSeconds < :to")
    fun getInTimeRange(from: Long, to: Long): List<Sensors>

    @Query("SELECT MAX(id) FROM sensors")
    fun getLastId(): Int

    @Query("SELECT * FROM sensors WHERE id = (SELECT MAX(id) FROM sensors)")
    fun getLastSensor(): Sensors

    @Query("SELECT * FROM sensors WHERE id =:id")
    fun getSensors(id: Int): Sensors

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(sensors: Sensors)

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insertAll(sensorsList: List<Sensors>)

    @Query("DELETE FROM sensors")
    fun wipe()
}