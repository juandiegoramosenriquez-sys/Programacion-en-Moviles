package com.ramos.avisoscampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AvisoEntity::class], version = 1, exportSchema = false)
abstract class AvisosDatabase : RoomDatabase() {

    abstract fun avisoDao(): AvisoDao

    companion object {
        @Volatile
        private var instancia: AvisosDatabase? = null

        fun obtener(context: Context): AvisosDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AvisosDatabase::class.java,
                    "avisoscampus.db"
                ).build().also { instancia = it }
            }
    }
}
