package edu.moravian.csci215.tic_tac_toe

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Root composable hosting navigation and the shared scaffold.
 */
@Composable
fun App() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    MaterialTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = Title,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable<Title> {
                    TitleScreen(
                        snackbarHostState = snackbarHostState,
                        onStartGame = { _, _ ->
                            navController.navigate(Game) {
                                launchSingleTop = true
                            }
                        },
                    )
                }
                composable<Game> {
                    GameScreen()
                }
                composable<End> {
                    EndScreen()
                }
            }
        }
    }
}
