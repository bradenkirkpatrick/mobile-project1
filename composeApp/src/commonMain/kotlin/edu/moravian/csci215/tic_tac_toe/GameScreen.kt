package edu.moravian.csci215.tic_tac_toe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import edu.moravian.csci215.tic_tac_toe.game.*
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

@Composable
fun GameScreen(game: Game) {
    val player1 = GamePlayer(game.player1, game.player1Type)
    val player2 = GamePlayer(game.player2, game.player2Type)
    val board = remember { mutableStateOf(Board()) }

    while (!board.value.isGameOver) {
        val player = if (board.value.turn == 'X') player1.ai else player2.ai
        if (player is AIPlayer) {
            val move = player.findMove(board.value, board.value.turn)
            board.value = board.value.playPiece(move.first, move.second) ?: return
        } else {
            break // wait for human input
        }
    }
    BoardDisplay(board)
}

@Composable
fun BoardDisplay(board: MutableState<Board>) {
    Row {
        Column {
            TictactoeButton(Pair(0, 0), board)
            TictactoeButton(Pair(0, 1), board)
            TictactoeButton(Pair(0, 2), board)
        }
        Column {
            TictactoeButton(Pair(1, 0), board)
            TictactoeButton(Pair(1, 1), board)
            TictactoeButton(Pair(1, 2), board)
        }
        Column {
            TictactoeButton(Pair(2, 0), board)
            TictactoeButton(Pair(2, 1), board)
            TictactoeButton(Pair(2, 2), board)
        }
    }
}

@Composable
fun TictactoeButton(
    cords: Pair<Int, Int>,
    board: MutableState<Board>,
) {
    Button(
        onClick = {
            board.value = board.value.playPiece(cords.first, cords.second) ?: return@Button
        },
    ) {
        Text(board.value[cords.first, cords.second].toString())
    }
}
