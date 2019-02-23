package com.e.btex.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.e.btex.data.dao.SensorsDao
import com.e.btex.data.entity.Sensors

@Database(entities = [Sensors::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun sensorsDao(): SensorsDao
}