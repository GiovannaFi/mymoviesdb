package com.example.mymoviesdb

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesEndPoint {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = "79895f6aa304179d99b6bd86c5eb4b82",
        @Query("language") language: String = "it"
    ): Response<MovieData>
}