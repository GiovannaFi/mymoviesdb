package com.example.mymoviesdb

import com.example.mymoviesdb.dto.CreditsData
import com.example.mymoviesdb.dto.MovieData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesEndPoint {


    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = "79895f6aa304179d99b6bd86c5eb4b82",
        @Query("language") language: String = "it"
    ): Response<MovieData>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String = "79895f6aa304179d99b6bd86c5eb4b82",
        @Query("language") language: String = "it",
        @Query("query") query: String
    ): Response<MovieData>

    @GET("movie/{id}/credits")
    fun getMovieCast(
        @Path("id") movieId: Int = 447277,
        @Query("api_key") apiKey: String = "79895f6aa304179d99b6bd86c5eb4b82"
    ): Response<CreditsData>
}