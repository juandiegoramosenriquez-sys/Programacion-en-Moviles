package com.tecsup.catalogo

import android.content.Context
import com.tecsup.catalogo.data.auth.AuthRepository
import com.tecsup.catalogo.data.auth.FirebaseAuthRepository
import com.tecsup.catalogo.data.local.AppDatabase
import com.tecsup.catalogo.data.remote.RetrofitClient
import com.tecsup.catalogo.data.repository.ItemRepository
import com.tecsup.catalogo.data.repository.ItemRepositoryImpl

class AppContainer(context: Context) {

    private val baseDeDatos = AppDatabase.obtener(context)

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(RetrofitClient.api, baseDeDatos.itemDao())
    }

    val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository()
    }
}
