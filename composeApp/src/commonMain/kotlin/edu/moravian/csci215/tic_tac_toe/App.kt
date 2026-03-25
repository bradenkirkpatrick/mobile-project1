package edu.moravian.csci215.tic_tac_toe

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

/**
 * Root composable hosting navigation and the shared scaffold.
 */
@Composable
fun App() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    MaterialTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
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
                        onStartGame = { player1, player2 ->
                            val game = Game(player1.name, player2.name, player1.type, player2.type)
                            navController.navigate(game)
                        },
                    )
                }
                composable<Game> {
                    thegame ->
                    val game = thegame.toRoute<Game>()
                    GameScreen(
                        game = game,
                        snackbarHostState = snackbarHostState,
                        onBack = { navController.popBackStack() },
                        onRoundOver = { end -> navController.navigate(end) },
                    )
                }
                composable<End> {
                    endEntry ->
                    val end = endEntry.toRoute<End>()
                    EndScreen(
                        end = end,
                        onBackToTitle = {
                            navController.navigate(Title) {
                                popUpTo(Title) { inclusive = true }
                            }
                        },
                        onPlayAgain = { nextGame ->
                            navController.navigate(nextGame) {
                                popUpTo<End> { inclusive = true }
                            }
                        },
                    )
                }
            }
        }
    }
}
