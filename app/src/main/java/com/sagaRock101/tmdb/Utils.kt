package com.sagaRock101.tmdb

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.io.IOException

object Utils {

    fun <T : ViewDataBinding> binder(layout: Int, parent: ViewGroup): T {
        return DataBindingUtil.inflate<T>(
            LayoutInflater.from(parent?.context)!!,
            layout, parent, false)
    }

    fun isConnectedOrConnecting(context: Context?) : Boolean {
        val connectivityManager = context?.getSystemService(
            Context
            .CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    @Throws(InterruptedException::class, IOException::class)
    fun isConnected(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }

    fun showToast(ctx: Context, msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }
}