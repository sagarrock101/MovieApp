package com.example.tmdb.api

import com.example.tmdb.BuildConfig

class AppConstants {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        var TMDB_API_KEY = BuildConfig.TMDB_API_KEY
        const val  IMAGE_URL = "http://image.tmdb.org/t/p/w500/"
    }
}
