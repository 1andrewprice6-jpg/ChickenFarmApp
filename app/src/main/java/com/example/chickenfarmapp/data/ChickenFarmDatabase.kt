package com.example.chickenfarmapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Chicken::class, EggLog::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ChickenFarmDatabase : RoomDatabase() {
    abstract fun chickenDao(): ChickenDao
    abstract fun eggLogDao(): EggLogDao

    companion object {
        @Volatile
        private var INSTANCE: ChickenFarmDatabase? = null

        fun getDatabase(context: Context): ChickenFarmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChickenFarmDatabase::class.java,
                    "chicken_farm_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
