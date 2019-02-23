package com.e.btex.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.e.btex.data.entity.Sensors

@Dao
interface SensorsDao {


    @Query("SELECT * FROM sensors")
    fun getAllSernsors(): LiveData<List<Sensors>>

    @Query("SELECT * FROM Sensors WHERE time >= :from AND time < :to")
    fun getInTimeRange(from: Long, to: Long): LiveData<List<Sensors>>

    @Insert
    fun insertAll(vararg sensors: Sensors)

    @Query("DELETE FROM sensors")
    fun wipe()

    @Query("SELECT MAX(id) FROM sensors")
    fun getLastId(): Int

}