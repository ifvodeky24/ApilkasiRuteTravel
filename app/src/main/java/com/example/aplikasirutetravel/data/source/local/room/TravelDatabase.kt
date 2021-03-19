package com.example.aplikasirutetravel.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aplikasirutetravel.data.source.local.entity.KondisiJalanEntity
import com.example.aplikasirutetravel.data.source.local.entity.PerusahaanEntity

@Database(
    entities = [PerusahaanEntity::class, KondisiJalanEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TravelDatabase : RoomDatabase() {

    abstract fun travelDao(): TravelDao

    companion object {

        @Volatile
        private var INSTANCE: TravelDatabase? = null

        fun getInstance(context: Context): TravelDatabase {
            if (INSTANCE == null) {
                synchronized(TravelDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            TravelDatabase::class.java, "Travel.db"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE as TravelDatabase
        }
    }
}