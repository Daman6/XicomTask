package com.example.innobuzztask.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.innobuzztask.repo.DataRepo

class DataViewModelProviderFactory(val repo: DataRepo,val context: Application):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DataViewModel(repo,context) as T
    }
}