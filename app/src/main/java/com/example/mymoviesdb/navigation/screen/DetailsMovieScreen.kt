package com.example.mymoviesdb.navigation.screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymoviesdb.MovieViewModel
import com.example.mymoviesdb.R
import com.example.mymoviesdb.network.dto.Response
import com.example.mymoviesdb.ui.theme.PurpleDark
import com.example.mymoviesdb.ui.theme.PurpleLight
import com.example.mymoviesdb.widget.CastList
import com.example.mymoviesdb.widget.CoilImage
import com.example.mymoviesdb.widget.RelatedList

class DetailsMovieScreen {


    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun ScreenMain(viewModel: MovieViewModel?, navController: NavController) {
        val gradientBrush = Brush.verticalGradient(
            colors = if (isSystemInDarkTheme()) listOf(
                PurpleDark,
                Color.Black
            ) else listOf(PurpleLight, Color.White)
        )



        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .background(
                    brush = gradientBrush
                )

        ) {
            val defaultImage = R.drawable.img_1
            val details = viewModel?.detailsMovie

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                shape = RoundedCornerShape(15.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    CoilImage(
                        posterPath = details?.poster_path,
                        defaultImage = defaultImage,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)

                    ) {
                        Text(
                            text = details?.original_title.orEmpty(),
                            style = TextStyle(
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = "${(details?.release_date)?.substring(0, 4)}",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 18.sp
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        details?.vote_average?.let { RatingStars(it) }

                        Text(
                            text = "(${details?.vote_count})",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 15.sp
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.cast),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 16.dp)
            )
            CastDetails(viewModel, details?.id)

            Text(
                text = stringResource(R.string.similar_movies),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 16.dp)
            )
            RelatedMovies(viewModel, details?.id, navController)

        }
    }
}


@Composable
fun RatingStars(vote: Double) {
    val maxRating = 5
    val fullStarIcon = Icons.Default.Star
    val emptyStarIcon = Icons.Default.StarBorder
    val halfStar = Icons.Default.StarHalf

    val fullStars = (vote / 2).toInt()
    val hasHalfStar = (vote % 2 != 0.0)

    val remainingStars = maxRating - fullStars - if (hasHalfStar) 1 else 0

    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(fullStars) {
            Icon(
                imageVector = fullStarIcon,
                contentDescription = stringResource(R.string.full_stars)
            )
        }

        if (hasHalfStar) {
            Icon(
                imageVector = halfStar,
                contentDescription = stringResource(R.string.half_stars)
            )
        }

        repeat(remainingStars) {
            Icon(
                imageVector = emptyStarIcon,
                contentDescription = stringResource(R.string.empty_star)
            )
        }
    }


}

@Composable
fun CastDetails(viewModel: MovieViewModel?, movieId: Int?) {
    LaunchedEffect(key1 = "cast", block = {
        if (movieId != null) {
            viewModel?.getCast(movieId)
        }
    })
    val defaultImage = R.drawable.img_1
    when (val resultFlow = viewModel?.castFlow?.collectAsState()?.value) {
        is Response.Loading -> {
            // Gestisci il caricamento
        }
        is Response.Success -> {
            Column {
                CastList(
                    cast = resultFlow.body.orEmpty(),
                    defaultImage = defaultImage
                )
            }
        }
        is Response.Error -> {
            Toast.makeText(LocalContext.current, stringResource(R.string.retry), Toast.LENGTH_SHORT)
                .show()
            Log.e("MainActivity", "Error: ${resultFlow.message}")
        }
        else -> {}
    }
}


@Composable
fun RelatedMovies(viewModel: MovieViewModel?, movieId: Int?, navController: NavController) {
    LaunchedEffect(key1 = "related", block = {
        if (movieId != null) {
            viewModel?.getSimilarMovies(movieId)
        }
    })
    val defaultImage = R.drawable.img_1
    when (val resultFlow = viewModel?.similarFlow?.collectAsState()?.value) {
        is Response.Loading -> {
            // Gestisci il caricamento
        }
        is Response.Success -> {
            Column {
                RelatedList(
                    similarMovies = resultFlow.body.orEmpty(),
                    defaultImage = defaultImage,
                    onElementClick = {
                        viewModel.detailsMovie = it
                        navController.navigate("details")
                    }
                )
            }
        }
        is Response.Error -> {
            Toast.makeText(LocalContext.current, stringResource(R.string.retry), Toast.LENGTH_SHORT)
                .show()
            Log.e("MainActivity", "Error: ${resultFlow.message}")
        }
        else -> {
            Toast.makeText(LocalContext.current, stringResource(R.string.retry), Toast.LENGTH_SHORT)
                .show()
        }
    }
}


//@Composable
//@Preview
//fun PreviewDetailsMovieScreen() {
//    Box(
//        Modifier
//            .fillMaxSize()
//            .background(Color.White)) {
//        DetailsMovieScreen().ScreenMain(viewModel = null)
//    }
//
//}