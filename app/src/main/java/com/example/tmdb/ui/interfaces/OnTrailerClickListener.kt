package com.example.tmdb.ui.interfaces

import com.example.tmdb.model.MovieTrailer

interface OnTrailerClickListener {
    fun onTrailerClicked(item: MovieTrailer)
}