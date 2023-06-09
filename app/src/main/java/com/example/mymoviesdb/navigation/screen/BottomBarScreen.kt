package com.example.mymoviesdb.navigation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mymoviesdb.MovieViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomNavigationScreen(viewModel: MovieViewModel, navController: NavController) {
    var selectedItem by remember { mutableStateOf("home") }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.height(56.dp)
            ) {
                BottomNavigationItem(
                    selected = selectedItem == "home",
                    onClick = { selectedItem = "home" },
                    label = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
                )
                BottomNavigationItem(
                    selected = selectedItem == "search",
                    onClick = { selectedItem = "search" },
                    label = { Text("search") },
                    icon = { Icon(Icons.Default.Search, contentDescription = "search") }
                )
                BottomNavigationItem(
                    selected = selectedItem == "favorites",
                    onClick = { selectedItem = "favorites" },
                    label = { Text("preferiti") },
                    icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "favorites") }
                )

            }
        },
        content = {
            when (selectedItem) {
                "home" -> {
                    PopularMoviesScreen().screenMain(viewModel, navController)

                }
                "search" -> {
                    SearchMoviesScreen().screenMain(viewModel, navController)

                }
                "favorites" ->{
                    FavoritesScreen().ScreenMain()
                }
            }
        }
    )
}





