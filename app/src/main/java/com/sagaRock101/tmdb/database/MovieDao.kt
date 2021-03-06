package com.sagaRock101.tmdb.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagaRock101.tmdb.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Query("delete from movie_table where id = :id")
    suspend fun deleteMovie(id: Int)

    @Query("SELECT * FROM movie_table WHERE id = :id")
    fun getMovie(id: Int): LiveData<Movie>

    @Query("SELECT * FROM movie_table")
    fun getMovieList(): LiveData<List<Movie>>
}