package com.sagaRock101.tmdb.ui.interfaces

import com.sagaRock101.tmdb.model.MovieTrailer

interface OnTrailerClickListener {
    fun onTrailerClicked(item: MovieTrailer)
}