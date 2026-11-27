package com.ramos.avisoscampus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AvisoDao {

    @Query("SELECT * FROM avisos ORDER BY id DESC")
    fun todos(): Flow<List<AvisoEntity>>

    @Insert
    suspend fun insertar(aviso: AvisoEntity): Long
}
