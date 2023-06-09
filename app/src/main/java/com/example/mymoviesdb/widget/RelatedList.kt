package com.example.mymoviesdb.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymoviesdb.network.dto.Result


@Composable
fun RelatedList(
    similarMovies: List<Result>,
    defaultImage: Int,
    onElementClick  : (Result) -> Unit
) {
    LazyRow(

    ) {
        itemsIndexed(similarMovies) { _, movie ->
            Card(
                modifier = Modifier
                    .height(200.dp)
                    .width(160.dp)
                    .padding(2.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = 5.dp,

            ) {
                CoilImageCast(movie.poster_path, defaultImage)
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable{
                            onElementClick(movie)
                        },
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
        }
    }
}



