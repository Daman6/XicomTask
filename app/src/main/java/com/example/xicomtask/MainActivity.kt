package com.example.xicomtask

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import com.example.innobuzztask.repo.DataRepo
import com.example.innobuzztask.utils.Resource
import com.example.innobuzztask.viewModel.DataViewModel
import com.example.innobuzztask.viewModel.DataViewModelProviderFactory
import com.example.newsapi.db.UserDatabase

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: DataViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val repo = DataRepo(UserDatabase(this))
        val viewModelProviderFactory = DataViewModelProviderFactory(repo,application)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(DataViewModel::class.java)

    }
}