package edu.moravian.csci215.tic_tac_toe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable
data class End(
    val player1: String,
    val player2: String,
    val player1Type: String,
    val player2Type: String,
    val player1Wins: Int,
    val player2Wins: Int,
    val ties: Int,
    val outcome: String,
    val winnerName: String?,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndScreen(
    end: End,
    onPlayAgain: (Game) -> Unit,
) {
    val resultMessage =
        when (end.outcome) {
            "PLAYER_1_WIN" -> "Player 1 won this round. ${end.winnerName} claimed the board as X."
            "PLAYER_2_WIN" -> "Player 2 won this round. ${end.winnerName} claimed the board as O."
            else -> "This round ended in a tie. Neither player completed three in a row."
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Over") },
                navigationIcon = {
                    TextButton(
                        onClick = {
                            onPlayAgain(
                                Game(
                                    player1 = end.player1,
                                    player2 = end.player2,
                                    player1Type = end.player1Type,
                                    player2Type = end.player2Type,
                                    player1Wins = end.player1Wins,
                                    player2Wins = end.player2Wins,
                                    ties = end.ties,
                                ),
                            )
                        },
                    ) {
                        Text("Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = resultMessage,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Overall score: ${end.player1} has ${end.player1Wins} win(s), ${end.player2} has ${end.player2Wins} win(s), and there have been ${end.ties} tie(s).",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = {
                    onPlayAgain(
                        Game(
                            player1 = end.player1,
                            player2 = end.player2,
                            player1Type = end.player1Type,
                            player2Type = end.player2Type,
                            player1Wins = end.player1Wins,
                            player2Wins = end.player2Wins,
                            ties = end.ties,
                        ),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Play Again")
            }
        }
    }
}
