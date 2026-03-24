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
import org.jetbrains.compose.resources.stringResource
import tictactoe.composeapp.generated.resources.Res
import tictactoe.composeapp.generated.resources.*

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
            "PLAYER_1_WIN" -> "${stringResource(Res.string.resultMessagePlayer1Part1)} ${end.winnerName} " +
                    stringResource(Res.string.resultMessagePlayer1Part2)
            "PLAYER_2_WIN" -> "${stringResource(Res.string.resultMessagePlayer2Part1)} ${end.winnerName} " +
                    stringResource(Res.string.resultMessagePlayer2Part2)
            else -> stringResource(Res.string.resultMessageDraw)
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.gameOver)) },
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
                        Text(stringResource(Res.string.backToTitle))
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
                text = stringResource(Res.string.overallScore1) + end.player1
                        + stringResource(Res.string.overallScore2) + end.player1Wins
                        + stringResource(Res.string.overallScore3) + end.player2
                        + stringResource(Res.string.overallScore4) + end.player2Wins
                        + stringResource(Res.string.overallScore5) + end.ties + stringResource(Res.string.overallScore6),
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
                Text(stringResource(Res.string.playAgain))
            }
        }
    }
}
