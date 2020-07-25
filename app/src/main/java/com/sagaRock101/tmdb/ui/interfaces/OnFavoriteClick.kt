package com.sagaRock101.tmdb.ui.interfaces

import com.sagaRock101.tmdb.model.Movie

interface OnFavoriteClick {
    fun onFavoriteClicked(item: Movie)
}