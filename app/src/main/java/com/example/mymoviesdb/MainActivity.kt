package com.example.mymoviesdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.Coil
import coil.ImageLoader
import com.example.mymoviesdb.navigation.screen.BottomNavigationScreen
import com.example.mymoviesdb.navigation.screen.DetailsMovieScreen
import com.example.mymoviesdb.navigation.screen.FavoritesScreen
import com.example.mymoviesdb.ui.theme.MyMoviesDbTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        Coil.setImageLoader {
            ImageLoader.Builder(applicationContext)
                .build()
        }


        setContent {
            MyMoviesDbTheme(){
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { BottomNavigationScreen(viewModel, navController) }
                    composable("details") { DetailsMovieScreen().ScreenMain(viewModel, navController) }
                    composable("favorites") { FavoritesScreen().ScreenMain() }
                }
            }

        }

    }
}


