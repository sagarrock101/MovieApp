package com.example.tmdb.broadcastReciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.tmdb.Utils.isConnected
import com.example.tmdb.Utils.isConnectedOrConnecting
import com.example.tmdb.ui.interfaces.InternetChecker

class NetworkBroadcastReceiver : BroadcastReceiver() {

    lateinit var internetChecker: InternetChecker

    fun setListener(listener: InternetChecker) {
        internetChecker = listener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if(isConnected() && isConnectedOrConnecting(context)) {
            internetChecker.isInternetAvailable(true)
        } else {
            internetChecker.isInternetAvailable(false)
        }
    }
}