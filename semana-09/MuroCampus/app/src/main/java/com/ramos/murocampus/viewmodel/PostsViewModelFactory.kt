package com.ramos.murocampus.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramos.murocampus.data.PostRepositoryRemote
import com.ramos.murocampus.data.local.MuroDatabase
import com.ramos.murocampus.data.remote.RetrofitClient

class PostsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = MuroDatabase.obtener(context).postDao()
        return PostsViewModel(PostRepositoryRemote(RetrofitClient.api, dao)) as T
    }
}
