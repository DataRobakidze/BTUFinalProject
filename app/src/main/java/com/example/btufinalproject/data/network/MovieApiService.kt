package com.example.btufinalproject.data.network

import com.example.btufinalproject.data.models.MovieResult
import com.example.btufinalproject.data.models.MovieDetail
import com.example.btufinalproject.data.models.MovieTrailers
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MovieResult

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetail

    @GET("movie/{id}/videos")
    suspend fun getMovieTrailers(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieTrailers
}
