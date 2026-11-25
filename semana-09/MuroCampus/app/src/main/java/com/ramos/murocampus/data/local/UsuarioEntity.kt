package com.ramos.murocampus.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import com.ramos.murocampus.model.Usuario

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val username: String
)

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios ORDER BY id")
    suspend fun todos(): List<UsuarioEntity>

    @Upsert
    suspend fun guardar(usuarios: List<UsuarioEntity>)
}

fun UsuarioEntity.aUsuario(): Usuario = Usuario(id = id, name = name, email = email, username = username)

fun Usuario.aEntity(): UsuarioEntity = UsuarioEntity(id = id, name = name, email = email, username = username)
