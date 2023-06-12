package com.example.mymoviesdb.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.mymoviesdb.R
import com.example.mymoviesdb.SharedImplementation
import com.example.mymoviesdb.network.dto.Result
import com.example.mymoviesdb.ui.theme.PurpleDark


@Composable
fun MovieList(
    modifier: Modifier = Modifier,
    movies: List<Result>,
    defaultImage: Int,
    onElementClick: (Result) -> Unit,
) {

    val context = LocalContext.current
    val selectedMovies = remember { mutableStateListOf<Result>() }
    val sharedImplementation = remember {
        SharedImplementation(context)
    }
    LaunchedEffect(Unit) {
        selectedMovies.addAll(sharedImplementation.getSelectedMovies())
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxSize()
    ) {
        itemsIndexed(movies) { _, movie ->
            val isLiked = selectedMovies.any { it.id == movie.id }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onElementClick(movie)
                        },
                    shape = RoundedCornerShape(15.dp),
                    elevation = 5.dp
                ) {
                    CoilImage(movie.poster_path, defaultImage)
                    Box(
                        modifier = Modifier
                            .padding(12.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            text = movie.title.orEmpty(),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 18.sp
                            ),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.TopStart)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp)
                        .clickable {
                            if (isLiked) {
                                selectedMovies.removeIf { it.id == movie.id }
                            } else {
                                selectedMovies.add(movie)
                            }
                            sharedImplementation.saveFavorites(selectedMovies)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.favorites),
                        tint = if (isLiked) PurpleDark else Color.White
                    )
                }

            }
        }
    }
}


@Composable
fun CoilImage(posterPath: String?, defaultImage: Int) {
    val imageUrl = posterPath?.let {
        "https://image.tmdb.org/t/p/w500$it"
    }

    val painter = imageUrl?.let {
        rememberImagePainter(
            data = it,
            builder = {

            }
        )
    }

    Box(
        modifier = Modifier
            .height(200.dp)
            .width(130.dp),
        contentAlignment = Alignment.Center
    ) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = stringResource(R.string.movie_poster),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.6f),
                                Color.Transparent
                            ), startY = 0f
                        )
                    )
            )
        } else {
            Image(
                painter = painterResource(defaultImage),
                contentDescription = stringResource(R.string.default_movie_poster),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}



