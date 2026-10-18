package com.tecsup.lavacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tecsup.lavacampus.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

@Dao
interface ServicioDao {
    @Query("SELECT * FROM servicios ORDER BY nombre")
    fun observarTodos(): Flow<List<Servicio>>

    @Query("SELECT * FROM servicios WHERE activo = 1 ORDER BY nombre")
    fun observarActivos(): Flow<List<Servicio>>

    @Query("SELECT * FROM servicios WHERE id = :id")
    suspend fun obtener(id: Int): Servicio?

    @Query("SELECT COUNT(*) FROM servicios")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(s: Servicio): Long

    @Insert
    suspend fun insertarTodos(lista: List<Servicio>)

    @Update
    suspend fun actualizar(s: Servicio)

    @Delete
    suspend fun eliminar(s: Servicio)

    @Query("UPDATE servicios SET activo = :activo WHERE id = :id")
    suspend fun cambiarActivo(id: Int, activo: Boolean)
}
