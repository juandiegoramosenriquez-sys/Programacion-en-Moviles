package com.ramos.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramos.murocampus.data.PostRepositoryRemote
import com.ramos.murocampus.data.remote.RetrofitClient

class PostsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PostsViewModel(PostRepositoryRemote(RetrofitClient.api)) as T
}
