package com.ramos.murocampus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ramos.murocampus.model.Post

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)

fun PostEntity.aPost(): Post = Post(userId = userId, id = id, title = title, body = body)

fun Post.aEntity(): PostEntity = PostEntity(id = id, userId = userId, title = title, body = body)
