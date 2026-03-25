package edu.moravian.csci215.tic_tac_toe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import edu.moravian.csci215.tic_tac_toe.game.AIPlayer
import edu.moravian.csci215.tic_tac_toe.game.Board
import edu.moravian.csci215.tic_tac_toe.game.Board.Companion.toStringRepresentation
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
import tictactoe.composeapp.generated.resources.ai_is_thinking
import tictactoe.composeapp.generated.resources.app_title
import tictactoe.composeapp.generated.resources.back
import tictactoe.composeapp.generated.resources.current_turn
import tictactoe.composeapp.generated.resources.gameoverTie
import tictactoe.composeapp.generated.resources.gameoverWin
import tictactoe.composeapp.generated.resources.notEmptyError
import tictactoe.composeapp.generated.resources.stillThinkingError

@Serializable
data class Game(
    val player1: String,
    val player2: String,
    val player1Type: PlayerType,
    val player2Type: PlayerType,
    val player1Wins: Int = 0,
    val player2Wins: Int = 0,
    val ties: Int = 0,
)

@Serializable
data class GamePlayer(
    val name: String,
    val type: PlayerType,
) {
    val ai: Player = when (type) {
        PlayerType.Human -> HumanPlayer()
        PlayerType.EasyAI -> EasyAIPlayer()
        PlayerType.MediumAI -> MediumAIPlayer()
        PlayerType.HardAI -> HardAIPlayer()
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

    val currentPlayer = if (board.turn == 'X') player1 else player2
    val aiTurn = !board.isGameOver && currentPlayer.ai is AIPlayer
    val stillThinkingError = stringResource(Res.string.stillThinkingError, currentPlayer.name, board.turn.toString())

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
                        finalBoard = board.toStringRepresentation(),
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
                        finalBoard = board.toStringRepresentation(),
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
                        finalBoard = board.toStringRepresentation(),
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
            aiTurn || aiThinking -> showError(stillThinkingError)
            !board.emptyAt(row, column) -> showError(notEmptyError)
            else -> {
                board = board.playPiece(row, column) ?: board
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.08f),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = { Text(stringResource(Res.string.app_title)) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                    Column(
                        modifier = Modifier.weight(0.85f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        BoardDisplay(
                            board = board,
                            onCellSelected = ::handleMove,
                            modifier = Modifier.fillMaxWidth(0.82f),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
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
                        modifier = Modifier.fillMaxWidth(0.92f),
                    )
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
            board.hasWon('X') -> stringResource(Res.string.gameoverWin, playerNameForPiece('X', xPlayer, oPlayer), "X")
            board.hasWon('O') -> stringResource(Res.string.gameoverWin, playerNameForPiece('O', xPlayer, oPlayer), "O")
            board.hasTied -> stringResource(Res.string.gameoverTie)
            aiThinking -> stringResource(Res.string.ai_is_thinking, currentPlayer.name, board.turn.toString())
            else -> stringResource(Res.string.current_turn, currentPlayer.name, board.turn.toString())
        }

    LuxeCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "LIVE MATCH",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PlayerMarkerCard(
                    label = "X",
                    name = xPlayer.name,
                    type = xPlayer.type.label,
                    active = board.turn == 'X' && !board.isGameOver,
                    modifier = Modifier.weight(1f),
                )
                PlayerMarkerCard(
                    label = "O",
                    name = oPlayer.name,
                    type = oPlayer.type.label,
                    active = board.turn == 'O' && !board.isGameOver,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

private fun playerNameForPiece(
    piece: Char,
    xPlayer: GamePlayer,
    oPlayer: GamePlayer,
): String = if (piece == 'X') xPlayer.name else oPlayer.name

@Composable
internal fun BoardDisplay(
    board: Board,
    onCellSelected: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    LuxeCard(
        modifier = modifier.aspectRatio(1f),
        padding = PaddingValues(16.dp),
        expandContent = true,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
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
                    enabled = enabled,
                )
            }
        }
    }
}

@Composable
internal fun TictactoeButton(
    piece: Char,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    val isX = piece == 'X'
    val isO = piece == 'O'
    val containerColor =
        when {
            isX -> MaterialTheme.colorScheme.primary.copy(alpha = 0.92f)
            isO -> MaterialTheme.colorScheme.secondaryContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        }
    val contentColor =
        when {
            isX -> MaterialTheme.colorScheme.onPrimary
            isO -> MaterialTheme.colorScheme.onSecondaryContainer
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        }

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier =
            Modifier
                .fillMaxSize()
                .aspectRatio(1f),
        shape = RoundedCornerShape(20.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = containerColor,
                disabledContentColor = contentColor,
            ),
    ) {
        Text(
            text = if (piece == ' ') "" else piece.toString(),
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Composable
private fun PlayerMarkerCard(
    label: String,
    name: String,
    type: String,
    active: Boolean,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (active) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant
    val background =
        if (active) {
            Brush.verticalGradient(
                colors =
                    listOf(
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.surface,
                    ),
            )
        } else {
            Brush.verticalGradient(
                colors =
                    listOf(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.colorScheme.surface,
                    ),
            )
        }

    Column(
        modifier =
            modifier
                .clip(RoundedCornerShape(22.dp))
                .background(background)
                .border(1.dp, borderColor, RoundedCornerShape(22.dp))
                .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = type,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
