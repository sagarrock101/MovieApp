package com.example.tmdb.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ViewModelFactory @Inject constructor(var application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            return MoviesViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}