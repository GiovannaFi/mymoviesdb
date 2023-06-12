package com.example.mymoviesdb.navigation.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymoviesdb.MovieViewModel
import com.example.mymoviesdb.R
import com.example.mymoviesdb.network.dto.Response
import com.example.mymoviesdb.network.dto.Result
import com.example.mymoviesdb.ui.theme.PurpleDark
import com.example.mymoviesdb.widget.MovieList


class SearchMoviesScreen {

    val SEARCH_RESPONSE_KEY = "popular movies"
    val fontFamily = FontFamily(
        Font(R.font.myfont)
    )

    @Composable
    fun screenMain(viewModel: MovieViewModel, navController: NavController) {
        LaunchedEffect(key1 = SEARCH_RESPONSE_KEY, block = {
            viewModel.getSearchedMovie(null)
        })
        val defaultImage = R.drawable.img_1
        when (val responseSearchMovie = viewModel.searchFlow.collectAsState().value) {
            is Response.Loading -> {
                // Gestisci il caricamento
            }
            is Response.Success -> {

                responseSearchMovie.body?.let {
                    SearchScreen(
                        it,
                        defaultImage,
                        viewModel,
                        navController
                    )
                }

            }
            is Response.Error -> {
                Toast.makeText(LocalContext.current, "Ritenta", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Error: ${responseSearchMovie.message}")
            }
        }

    }


    @Composable
    fun SearchScreen(
        movies: List<Result>,
        defaultImage: Int,
        viewModel: MovieViewModel,
        navController: NavController,
    ) {
        Column(modifier = Modifier.padding(bottom = 56.dp)) {
            Text(
                text = "Cerca un film:",
                fontSize = 25.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                color = PurpleDark,
                modifier = Modifier
                    .padding(bottom = 8.dp, top = 20.dp)
                    .align(Alignment.CenterHorizontally)

            )
            val query = remember { mutableStateOf("") }  //QUA

            val onQueryTextChange: (String) -> Unit = { newText ->
                if (newText.isNotEmpty()) {
                    viewModel.getSearchedMovie(newText)
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = 5.dp,

                ) {
                TextField(
                    value = query.value,
                    onValueChange = { value ->
                        query.value = value
                        onQueryTextChange(value)
                    },
                    placeholder = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Icona di ricerca",
                                modifier = Modifier.size(24.dp),
                                tint = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cerca un film",
                                style = TextStyle(color = Color.DarkGray)
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.White),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                )
            }
            MovieList(
                movies = movies,
                defaultImage = defaultImage,
                onElementClick = {
                    viewModel.detailsMovie = it
                    navController.navigate("details")
                }
            )
        }
    }

}

