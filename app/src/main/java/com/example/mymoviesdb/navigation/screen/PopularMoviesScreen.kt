package com.example.mymoviesdb.navigation.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymoviesdb.MovieViewModel
import com.example.mymoviesdb.R
import com.example.mymoviesdb.network.dto.Response
import com.example.mymoviesdb.ui.theme.PurpleDark
import com.example.mymoviesdb.widget.MovieList

class PopularMoviesScreen {

    val POPULAR_RESPONSE_KEY = "popular movies"
    val fontFamily = FontFamily(
        Font(R.font.myfont)
    )

    @Composable
    fun screenMain(
        viewModel: MovieViewModel,
        navController: NavController,
    ) {
        LaunchedEffect(key1 = POPULAR_RESPONSE_KEY, block = {
            viewModel.getPopularMovies()
        })
        val defaultImage = R.drawable.img_1
        when (val resultFlow = viewModel.moviesFlow.collectAsState().value) {
            is Response.Loading -> {
                // Gestisci il caricamento
            }
            is Response.Success -> {
                Column(modifier = Modifier.padding(bottom = 56.dp)) {
                    Text(
                        text = "Film popolari oggi",
                        fontSize = 28.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = PurpleDark,
                        modifier = Modifier
                            .padding(bottom = 8.dp, top = 20.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    MovieList(movies = resultFlow.body.orEmpty(),
                        defaultImage = defaultImage,
                        onElementClick = {
                            viewModel.detailsMovie = it
                            navController.navigate("details")
                        }
                    )
                }
            }
            is Response.Error -> {
                Toast.makeText(LocalContext.current, "Ritenta", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Error: ${resultFlow.message}")
            }
        }
    }

}
