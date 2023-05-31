package com.example.mymoviesdb

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.Coil
import coil.ImageLoader
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Coil.setImageLoader {
            ImageLoader.Builder(applicationContext)
                .build()
        }

        observeMoviesFlow()
        viewModel.getPopularMovies()

        Coil.setImageLoader {
            ImageLoader.Builder(applicationContext)
                .build()
        }
    }

    private fun observeMoviesFlow() {
        lifecycleScope.launch {
            viewModel.moviesFlow.collect { response ->
                when (response) {
                    is Response.Loading -> {
                        // Gestisci il caricamento
                    }
                    is Response.Success -> {
                        setContent {
                            MovieList(movies = response.body.orEmpty())
                        }
                    }
                    is Response.Error -> {
                        // Gestisci l'errore
                        Log.e("MainActivity", "Error: ${response.message}")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieList(movies: List<Result>) {

    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
        itemsIndexed(movies) { _, movie ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    elevation = 5.dp
                ){
                    CoilImage(movie.poster_path)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                    Text(
                        text = "${movie.original_title}",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp),
                        fontWeight = FontWeight.Bold,
                    )
                }}

            }
        }
    }
}

@Composable
fun CoilImage(posterPath: String?) {
    Box(
        modifier = Modifier
            .height(180.dp)
            .width(130.dp),
        contentAlignment = Alignment.Center
    ) {
        val imageUrl = posterPath?.let {
            "https://image.tmdb.org/t/p/w500$it"
        }

        imageUrl?.let {
            val painter = rememberImagePainter(
                data = it,
                builder = {

                }
            )
            Image(
                painter = painter,
                contentDescription = "Movie Poster",
                contentScale = ContentScale.Crop
            )
        }
    }
}
