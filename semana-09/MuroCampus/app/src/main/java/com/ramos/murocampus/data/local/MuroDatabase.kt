package com.ramos.murocampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PostEntity::class, UsuarioEntity::class], version = 3, exportSchema = false)
abstract class MuroDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var instancia: MuroDatabase? = null

        fun obtener(context: Context): MuroDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    MuroDatabase::class.java,
                    "murocampus.db"
                ).fallbackToDestructiveMigration(true)
                    .build()
                    .also { instancia = it }
            }
    }
}
