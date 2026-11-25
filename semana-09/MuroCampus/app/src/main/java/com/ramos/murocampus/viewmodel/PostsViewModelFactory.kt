package com.ramos.murocampus.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramos.murocampus.data.PostRepositoryRemote
import com.ramos.murocampus.data.local.MuroDatabase
import com.ramos.murocampus.data.local.PreferenciasTema
import com.ramos.murocampus.data.remote.RetrofitClient

class PostsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemaViewModel::class.java)) {
            return TemaViewModel(PreferenciasTema(context)) as T
        }
        val base = MuroDatabase.obtener(context)
        return PostsViewModel(PostRepositoryRemote(RetrofitClient.api, base.postDao(), base.usuarioDao())) as T
    }
}
