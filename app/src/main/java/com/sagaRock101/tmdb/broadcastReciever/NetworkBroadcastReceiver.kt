package com.sagaRock101.tmdb.broadcastReciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sagaRock101.tmdb.Utils.isConnected
import com.sagaRock101.tmdb.Utils.isConnectedOrConnecting
import com.sagaRock101.tmdb.ui.interfaces.InternetChecker

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