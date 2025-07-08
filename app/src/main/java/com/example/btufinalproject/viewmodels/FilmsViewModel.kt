package com.example.btufinalproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btufinalproject.data.models.Movie
import com.example.btufinalproject.data.models.MovieDetail
import com.example.btufinalproject.data.models.MovieResult
import com.example.btufinalproject.data.models.MovieTrailers
import com.example.btufinalproject.data.network.RetrofitInstance
import kotlinx.coroutines.launch
import android.util.Log

class FilmsViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>(emptyList())
    val movies: LiveData<List<Movie>> = _movies

    private var currentPage = 1
    private val itemsPerPage = 20
    private val maxPagesToFetch = 30
    private var isLoading = false

    private val apiKey = "c7ace6bdb313416da5e4820264823aa0"

    fun fetchMovies() {
        if (isLoading || currentPage > maxPagesToFetch) return

        isLoading = true
        viewModelScope.launch {
            try {
                val movieResult: MovieResult = RetrofitInstance.api.getPopularMovies(apiKey, currentPage)
                val updatedList = _movies.value.orEmpty() + movieResult.results
                _movies.postValue(updatedList)

                for (movie in movieResult.results) {
                    fetchMovieDetail(movie.id)
                    fetchMovieTrailers(movie.id)
                }

                currentPage++

                if (updatedList.size % itemsPerPage == 0 && currentPage <= maxPagesToFetch) {
                    fetchMovies()
                }

            } catch (e: Exception) {
                Log.e("FilmsViewModel", "Fetch failed: ${e.localizedMessage}")
            } finally {
                isLoading = false
            }
        }
    }

    private fun fetchMovieDetail(movieId: Int) {
        viewModelScope.launch {
            try {
                val movieDetail: MovieDetail = RetrofitInstance.api.getMovieDetail(movieId, apiKey)
                val updatedMovies = _movies.value?.map { movie ->
                    if (movie.id == movieId) {
                        movie.copy(
                            runtime = movieDetail.runtime,
                            genres = movieDetail.genres
                        )
                    } else movie
                }
                _movies.postValue(updatedMovies)
            } catch (e: Exception) {
                Log.e("FilmsViewModel", "Fetch detail failed: ${e.localizedMessage}")
            }
        }
    }

    private fun fetchMovieTrailers(movieId: Int) {
        viewModelScope.launch {
            try {
                val movieTrailers: MovieTrailers = RetrofitInstance.api.getMovieTrailers(movieId, apiKey)
                val trailerKey = movieTrailers.results.firstOrNull()?.key
                val updatedMovies = _movies.value?.map { movie ->
                    if (movie.id == movieId) {
                        movie.copy(trailerKey = trailerKey)
                    } else movie
                }
                _movies.postValue(updatedMovies)
            } catch (e: Exception) {
                Log.e("FilmsViewModel", "Fetch trailers failed: ${e.localizedMessage}")
            }
        }
    }
}
