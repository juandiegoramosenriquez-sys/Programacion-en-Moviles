package com.ramos.murocampus.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PostDao {

    @Query("SELECT * FROM posts WHERE propio = 0 ORDER BY id")
    suspend fun todos(): List<PostEntity>

    @Query("SELECT * FROM posts WHERE propio = 0 AND userId = :userId ORDER BY id")
    suspend fun porUsuario(userId: Int): List<PostEntity>

    @Query("SELECT * FROM posts WHERE propio = 1 ORDER BY id DESC")
    suspend fun propios(): List<PostEntity>

    @Query("SELECT * FROM posts WHERE propio = 1 AND userId = :userId ORDER BY id DESC")
    suspend fun propiosDe(userId: Int): List<PostEntity>

    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun porId(id: Int): PostEntity?

    @Query("SELECT MAX(id) FROM posts")
    suspend fun mayorId(): Int?

    @Query("UPDATE posts SET title = :titulo WHERE id = :id")
    suspend fun cambiarTitulo(id: Int, titulo: String)

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun borrar(id: Int)

    @Upsert
    suspend fun guardar(posts: List<PostEntity>)
}
