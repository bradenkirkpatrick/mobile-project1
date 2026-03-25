package edu.moravian.csci215.tic_tac_toe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import edu.moravian.csci215.tic_tac_toe.game.Board
import edu.moravian.csci215.tic_tac_toe.game.Board.Companion.createFromString
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import tictactoe.composeapp.generated.resources.Res
import tictactoe.composeapp.generated.resources.back
import tictactoe.composeapp.generated.resources.final_board
import tictactoe.composeapp.generated.resources.gameOver
import tictactoe.composeapp.generated.resources.overall_score
import tictactoe.composeapp.generated.resources.playAgain
import tictactoe.composeapp.generated.resources.result_draw
import tictactoe.composeapp.generated.resources.result_win

@Serializable
data class End(
    val player1: String,
    val player2: String,
    val player1Type: PlayerType,
    val player2Type: PlayerType,
    val player1Wins: Int,
    val player2Wins: Int,
    val ties: Int,
    val outcome: String,
    val winnerName: String?,
    val finalBoard: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndScreen(
    end: End,
    onBackToTitle: () -> Unit,
    onPlayAgain: (Game) -> Unit,
) {
    val scrollState = rememberScrollState()
    val finalBoard = Board.createFromString(end.finalBoard)
    val resultMessage =
        when (end.outcome) {
            "PLAYER_1_WIN" -> stringResource(Res.string.result_win, end.winnerName.orEmpty(), "X")
            "PLAYER_2_WIN" -> stringResource(Res.string.result_win, end.winnerName.orEmpty(), "O")
            else -> stringResource(Res.string.result_draw)
        }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.gameOver)) },
                navigationIcon = {
                    IconButton(onClick = onBackToTitle) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        BoxWithConstraints(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 32.dp),
        ) {
            val landscape = maxWidth > maxHeight

            if (landscape) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    EndSummary(
                        resultMessage = resultMessage,
                        end = end,
                        finalBoard = finalBoard,
                        modifier = Modifier.weight(1f),
                    )
                    PlayAgainPanel(
                        end = end,
                        onPlayAgain = onPlayAgain,
                        modifier = Modifier.weight(0.9f),
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    EndSummary(
                        resultMessage = resultMessage,
                        end = end,
                        finalBoard = finalBoard,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.weight(0.05f))
                    PlayAgainPanel(
                        end = end,
                        onPlayAgain = onPlayAgain,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun EndSummary(
    resultMessage: String,
    end: End,
    finalBoard: Board,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = resultMessage,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )
            Text(
                text =
                    stringResource(
                        Res.string.overall_score,
                        end.player1,
                        end.player1Wins,
                        end.player2,
                        end.player2Wins,
                        end.ties,
                    ),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(Res.string.final_board),
                style = MaterialTheme.typography.titleMedium,
            )
            BoardDisplay(
                board = finalBoard,
                onCellSelected = { _, _ -> },
                enabled = false,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .sizeIn(maxWidth = 240.dp),
            )
        }
    }
}

@Composable
private fun PlayAgainPanel(
    end: End,
    onPlayAgain: (Game) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.widthIn(max = 420.dp),
        shape = RoundedCornerShape(28.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = "Ready for another round?",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Replay with the same players and difficulty settings.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
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
