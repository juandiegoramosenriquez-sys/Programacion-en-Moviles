package com.tecsup.catalogo

import com.tecsup.catalogo.data.remote.RetrofitClient
import com.tecsup.catalogo.data.repository.ItemRepository
import com.tecsup.catalogo.data.repository.ItemRepositoryImpl

class AppContainer {
    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(RetrofitClient.api)
    }
}
