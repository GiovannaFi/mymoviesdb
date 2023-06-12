package com.example.mymoviesdb.navigation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.mymoviesdb.R
import com.example.mymoviesdb.SharedImplementation
import com.example.mymoviesdb.ui.theme.PurpleDark
import com.example.mymoviesdb.ui.theme.PurpleLight

class FavoritesScreen {
    @Composable
    fun ScreenMain() {
        val defaultImage = R.drawable.img_1
        val context = LocalContext.current
        val sharedImplementation = SharedImplementation(context)
        val gradientBrush = Brush.verticalGradient(
            colors = if (isSystemInDarkTheme()) listOf(
                PurpleDark,
                Color.Black
            ) else listOf(PurpleLight, Color.White)
        )

        val selectedMovies =
            remember { sharedImplementation.getSelectedMovies().toMutableStateList() }


        if (selectedMovies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradientBrush),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Lista dei preferiti vuota",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Thin,
                    textAlign = TextAlign.Center
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(bottom = 56.dp)
                .fillMaxHeight()
                .background(brush = gradientBrush)
        ) {
            itemsIndexed(selectedMovies) { index, movie ->
                val isLiked = selectedMovies.any { it.id == movie.id }



                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp),
                    elevation = 8.dp,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    CoilImageFavs(
                        movie.poster_path,
                        defaultImage
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = movie.title.orEmpty(),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(top = 16.dp, start = 10.dp)
                    )

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(32.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .clickable {
                                    if (isLiked) {
                                        selectedMovies.remove(movie)

                                    } else {
                                        selectedMovies.add(movie)
                                    }

                                    sharedImplementation.saveFavorites(selectedMovies)
                                },
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",

                            tint = if (isLiked) PurpleDark else Color.White
                        )
                    }
                }
            }
        }

    }
}


@Composable
fun CoilImageFavs(posterPath: String?, defaultImage: Int) {
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
            .fillMaxWidth()
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
                                Color.Black,
                                Color.Transparent
                            ),
                            startY = 0f
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

