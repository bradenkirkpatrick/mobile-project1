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

@Serializable
data class Game(
    val player1: String,
    val player2: String,
    val player1Type: String,
    val player2Type: String,
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
) {
    val player1 = GamePlayer(game.player1, game.player1Type)
    val player2 = GamePlayer(game.player2, game.player2Type)
    var board by remember { mutableStateOf(Board()) }
    var aiThinking by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val currentPlayer = if (board.turn == 'X') player1 else player2
    val aiTurn = !board.isGameOver && currentPlayer.ai is AIPlayer

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
            aiTurn || aiThinking -> showError("${currentPlayer.name} is still thinking. Wait for the AI to place ${board.turn}.")
            !board.emptyAt(row, column) -> showError("That space is already taken. Choose an empty square.")
            else -> {
                board = board.playPiece(row, column) ?: board
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tic-Tac-Toe") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
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
            board.hasWon('X') -> "Game over. ${playerNameForPiece(piece = 'X', xPlayer = xPlayer, oPlayer = oPlayer)} won with X."
            board.hasWon('O') -> "Game over. ${playerNameForPiece(piece = 'O', xPlayer = xPlayer, oPlayer = oPlayer)} won with O."
            board.hasTied -> "Game over. The board is full, so this round ended in a tie."
            aiThinking -> "${currentPlayer.name} is up now with ${board.turn} and is choosing a move."
            else -> "${currentPlayer.name} is the current player and will place ${board.turn}."
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
