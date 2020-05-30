package com.example.tmdb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

object Utils {
    fun <T : ViewDataBinding> binder(layout: Int, parent: ViewGroup): T {
        return DataBindingUtil.inflate<T>(
            LayoutInflater.from(parent?.context)!!,
            layout, parent, false)
    }
}