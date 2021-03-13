package com.sagaRock101.tmdb.api

import com.sagaRock101.tmdb.BuildConfig

class AppConstants {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        var TMDB_API_KEY = BuildConfig.TMDB_API_KEY
        const val  IMAGE_URL = "http://image.tmdb.org/t/p/w500"
        const val IMAGE_BACK_DROP = "https://image.tmdb.org/t/p/w780"

    }
}
