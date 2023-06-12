package com.example.mymoviesdb.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.mymoviesdb.R
import com.example.mymoviesdb.network.dto.Cast


@Composable
fun CastList(
    cast: List<Cast>,
    defaultImage: Int,
) {
    LazyRow(

    ) {
        itemsIndexed(cast) { _, cast ->
                Card(
                    modifier = Modifier
                        .height(200.dp)
                        .width(160.dp)
                        .padding(2.dp),
                    shape = RoundedCornerShape(15.dp),
                    elevation = 5.dp
                ) {
                    CoilImageCast(cast.profile_path, defaultImage)
                    Box(
                        modifier = Modifier
                            .padding(12.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            text = "${cast.name} \n(${cast.character})",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 18.sp
                            ),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.TopStart)
                        )
                    }
                }
            }
        }
    }


@Composable
fun CoilImageCast(castPath: String?, defaultImage: Int) {
    val imageUrl = castPath?.let {
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
            .height(160.dp)
            .width(200.dp),
        contentAlignment = Alignment.Center

    ) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = stringResource(R.string.cast_poster),
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
                contentDescription = stringResource(R.string.default_cast_poster),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
