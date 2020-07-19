package com.example.tmdb.ui.interfaces

import com.example.tmdb.model.Movie

interface OnFavoriteClick {
    fun onFavoriteClicked(item: Movie)
}