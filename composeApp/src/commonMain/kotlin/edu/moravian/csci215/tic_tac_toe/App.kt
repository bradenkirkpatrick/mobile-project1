package edu.moravian.csci215.tic_tac_toe

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
@Composable
fun App() {
    val navController = rememberNavController()
    MaterialTheme ()
    {
        Scaffold() { innerPadding ->
            NavHost(
                navController,
                startDestination = Game,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable<Title> {}
                composable<Game> {
                    GameScreen()
                }
                composable<End> {}
            }
        }
    }
}
