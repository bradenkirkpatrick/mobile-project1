package edu.moravian.csci215.tic_tac_toe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.moravian.csci215.tic_tac_toe.game.AIPlayer
import edu.moravian.csci215.tic_tac_toe.game.Board
import edu.moravian.csci215.tic_tac_toe.game.EasyAIPlayer
import edu.moravian.csci215.tic_tac_toe.game.HardAIPlayer
import edu.moravian.csci215.tic_tac_toe.game.HumanPlayer
import edu.moravian.csci215.tic_tac_toe.game.MediumAIPlayer
import edu.moravian.csci215.tic_tac_toe.game.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import tictactoe.composeapp.generated.resources.Res
import tictactoe.composeapp.generated.resources.*

@Serializable
data class Game(
    val player1: String,
    val player2: String,
    val player1Type: String,
    val player2Type: String,
    val player1Wins: Int = 0,
    val player2Wins: Int = 0,
    val ties: Int = 0,
)

@Serializable
data class GamePlayer(
    val name: String,
    val type: String,
) {
    val ai get() = getAI()

    fun getAI(): Player =
        when (type) {
            "Easy AI" -> EasyAIPlayer()
            "Medium AI" -> MediumAIPlayer()
            "Hard AI" -> HardAIPlayer()
            else -> HumanPlayer()
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    game: Game,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onRoundOver: (End) -> Unit,
) {
    val player1 = GamePlayer(game.player1, game.player1Type)
    val player2 = GamePlayer(game.player2, game.player2Type)
    var board by remember { mutableStateOf(Board()) }
    var aiThinking by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val notEmptyError = stringResource(Res.string.notEmptyError)
    val stillThinkingError = stringResource(Res.string.stillThinkingError)

    val currentPlayer = if (board.turn == 'X') player1 else player2
    val aiTurn = !board.isGameOver && currentPlayer.ai is AIPlayer

    LaunchedEffect(board.isGameOver) {
        if (!board.isGameOver) return@LaunchedEffect

        val endState =
            when {
                board.hasWon('X') ->
                    End(
                        player1 = game.player1,
                        player2 = game.player2,
                        player1Type = game.player1Type,
                        player2Type = game.player2Type,
                        player1Wins = game.player1Wins + 1,
                        player2Wins = game.player2Wins,
                        ties = game.ties,
                        outcome = "PLAYER_1_WIN",
                        winnerName = game.player1,
                    )
                board.hasWon('O') ->
                    End(
                        player1 = game.player1,
                        player2 = game.player2,
                        player1Type = game.player1Type,
                        player2Type = game.player2Type,
                        player1Wins = game.player1Wins,
                        player2Wins = game.player2Wins + 1,
                        ties = game.ties,
                        outcome = "PLAYER_2_WIN",
                        winnerName = game.player2,
                    )
                else ->
                    End(
                        player1 = game.player1,
                        player2 = game.player2,
                        player1Type = game.player1Type,
                        player2Type = game.player2Type,
                        player1Wins = game.player1Wins,
                        player2Wins = game.player2Wins,
                        ties = game.ties + 1,
                        outcome = "TIE",
                        winnerName = null,
                    )
            }

        onRoundOver(endState)
    }

    LaunchedEffect(board) {
        val activePlayer = if (board.turn == 'X') player1 else player2
        val player = activePlayer.ai
        if (!board.isGameOver && player is AIPlayer) {
            aiThinking = true
            delay(750)
            val move = player.findMove(board, board.turn)
            board = board.playPiece(move.first, move.second) ?: board
            aiThinking = false
        } else {
            aiThinking = false
        }
    }

    fun showError(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }
    fun handleMove(row: Int, column: Int) {
        when {
            board.isGameOver -> Unit
            aiTurn || aiThinking -> showError("${currentPlayer.name}${stillThinkingError}${board.turn}.")
            !board.emptyAt(row, column) -> showError(notEmptyError)
            else -> {
                board = board.playPiece(row, column) ?: board
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.app_title)) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(stringResource(Res.string.backToTitle))
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
                    .padding(20.dp),
        ) {
            val landscape = maxWidth > maxHeight

            if (landscape) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    GameStatus(
                        board = board,
                        xPlayer = player1,
                        oPlayer = player2,
                        currentPlayer = currentPlayer,
                        aiThinking = aiTurn || aiThinking,
                        modifier = Modifier.weight(1f),
                    )
                    BoardDisplay(
                        board = board,
                        onCellSelected = ::handleMove,
                        modifier = Modifier.weight(1.2f),
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    GameStatus(
                        board = board,
                        xPlayer = player1,
                        oPlayer = player2,
                        currentPlayer = currentPlayer,
                        aiThinking = aiTurn || aiThinking,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    BoardDisplay(
                        board = board,
                        onCellSelected = ::handleMove,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun GameStatus(
    board: Board,
    xPlayer: GamePlayer,
    oPlayer: GamePlayer,
    currentPlayer: GamePlayer,
    aiThinking: Boolean,
    modifier: Modifier = Modifier,
) {
    val message =
        when {
            board.hasWon('X') -> playerNameForPiece('X', xPlayer, oPlayer) + stringResource(Res.string.gameoverX)
            board.hasWon('O') -> playerNameForPiece('O', xPlayer, oPlayer) + stringResource(Res.string.gameoverO)
            board.hasTied ->  stringResource(Res.string.gameoverTie)
            aiThinking -> currentPlayer.name + stringResource(Res.string.aiIsThinking1) +
                    board.turn + stringResource(Res.string.aiIsThinking2)
            else -> "${currentPlayer.name} ${stringResource(Res.string.whosTurn)} ${board.turn}."
        }

    Text(
        text = message,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
    )
}

private fun playerNameForPiece(
    piece: Char,
    xPlayer: GamePlayer,
    oPlayer: GamePlayer,
): String = if (piece == 'X') xPlayer.name else oPlayer.name

@Composable
private fun BoardDisplay(
    board: Board,
    onCellSelected: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.aspectRatio(1f),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(4.dp),
        userScrollEnabled = false,
    ) {
        items(9) { index ->
            val row = index / 3
            val column = index % 3
            TictactoeButton(
                piece = board[row, column],
                onClick = { onCellSelected(row, column) },
            )
        }
    }
}

@Composable
private fun TictactoeButton(
    piece: Char,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.aspectRatio(1f),
        shape = RoundedCornerShape(20.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
    ) {
        Text(
            text = if (piece == ' ') "" else piece.toString(),
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}
