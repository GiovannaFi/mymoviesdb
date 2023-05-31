package com.example.mymoviesdb

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MovieViewModel : ViewModel() {

    private val _moviesFlow =
        MutableStateFlow<Response<List<Result>>>(Response.Loading)
    val moviesFlow: StateFlow<Response<List<Result>>> =
        _moviesFlow


    private var moviesEndPoint: MoviesEndPoint

    init {
        val builderOkHttp = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger.DEFAULT
            ).apply { level = HttpLoggingInterceptor.Level.BODY }).build()

        val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org/3//")
            .addConverterFactory(GsonConverterFactory.create())
            .client(builderOkHttp).build()


        moviesEndPoint = retrofit.create(MoviesEndPoint::class.java)
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            try {
                val response = moviesEndPoint.getPopularMovies()
                if (response.isSuccessful) {
                    val moviesResponse = response.body()
                    if (moviesResponse != null) {
                        _moviesFlow.value =
                            Response.Success(response.code(), moviesResponse.results)
                    } else {
                        _moviesFlow.value = Response.Error(response.code(), "Empty response body")
                    }
                } else {
                    _moviesFlow.value = Response.Error(response.code(), response.message())
                }
            } catch (e: Exception) {
                _moviesFlow.value = Response.Error(500, "There was an error")
                Log.e("MovieViewModel", "Error: ${e.message}")
            }
        }
    }
}

