package com.example.mymoviesdb

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoviesdb.network.MoviesEndPoint
import com.example.mymoviesdb.network.dto.Cast
import com.example.mymoviesdb.network.dto.Response
import com.example.mymoviesdb.network.dto.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieViewModel : ViewModel() {

    val saveScreen = MutableStateFlow("home")

    private val _moviesFlow =
        MutableStateFlow<Response<List<Result>>>(Response.Loading)
    val moviesFlow: StateFlow<Response<List<Result>>> =
        _moviesFlow

    private val _searchFlow =
        MutableStateFlow<Response<List<Result>>>(Response.Loading)
    val searchFlow: StateFlow<Response<List<Result>>> =
        _searchFlow

    private val _castFlow =
        MutableStateFlow<Response<List<Cast>>>(Response.Loading)
    val castFlow: StateFlow<Response<List<Cast>>> =
        _castFlow

    private val _similarFlow =
        MutableStateFlow<Response<List<Result>>>(Response.Loading)
    val similarFlow: StateFlow<Response<List<Result>>> =
        _similarFlow


    private var moviesEndPoint: MoviesEndPoint

    var detailsMovie: Result? = null

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

    fun getSearchedMovie(userSearch: String?) {
        viewModelScope.launch {
            try {
                val response = moviesEndPoint.searchMovies(query = userSearch.orEmpty())
                if (response.isSuccessful) {
                    val moviesResponse = response.body()
                    if (moviesResponse != null) {
                        _searchFlow.value =
                            Response.Success(response.code(), moviesResponse.results)
                    } else {
                        _searchFlow.value = Response.Error(response.code(), "Empty response body")
                    }
                } else {
                    _searchFlow.value = Response.Error(response.code(), response.message())
                }
            } catch (e: Exception) {
                _searchFlow.value = Response.Error(500, "There was an error")
                Log.e("MovieViewModel", "Error: ${e.message}")
            }
        }

    }

    fun getCast(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = moviesEndPoint.getMovieCast(movieId)
                if (response.isSuccessful) {
                    val moviesResponse = response.body()
                    if (moviesResponse != null) {
                        _castFlow.value =
                            Response.Success(response.code(), moviesResponse.cast)
                    } else {
                        _castFlow.value = Response.Error(response.code(), "Empty response body")
                    }
                } else {
                    _castFlow.value = Response.Error(response.code(), response.message())
                }
            } catch (e: Exception) {
                _castFlow.value = Response.Error(500, "There was an error")
                Log.e("MovieViewModel", "Error: ${e.message}")
            }
        }

    }

    fun getSimilarMovies(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = moviesEndPoint.getRelatedMovies(movieId)
                if (response.isSuccessful) {
                    val moviesResponse = response.body()
                    if (moviesResponse != null) {
                        _similarFlow.value =
                            Response.Success(response.code(), moviesResponse.results)
                    } else {
                        _castFlow.value = Response.Error(response.code(), "Empty response body")
                    }
                } else {
                    _castFlow.value = Response.Error(response.code(), response.message())
                }
            } catch (e: Exception) {
                _castFlow.value = Response.Error(500, "There was an error")
                Log.e("MovieViewModel", "Error: ${e.message}")
            }
        }

    }
}

