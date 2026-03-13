package edu.moravian.csci215.tic_tac_toe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import edu.moravian.csci215.tic_tac_toe.game.Board
import kotlinx.serialization.Serializable

@Serializable
data object Game

@Composable
fun GameScreen() {
    val board = remember { mutableStateOf(Board()) }
    BoardDisplay(board)
}

@Composable
fun BoardDisplay(board: MutableState<Board>, ) {
    Row{
        Column {
            TiktaktoeButton(Pair(0, 0), board)
            TiktaktoeButton(Pair(0, 1), board)
            TiktaktoeButton(Pair(0, 2), board)
        }
        Column {
            TiktaktoeButton(Pair(1, 0), board)
            TiktaktoeButton(Pair(1, 1), board)
            TiktaktoeButton(Pair(1, 2), board)
        }
        Column {
            TiktaktoeButton(Pair(2, 0), board)
            TiktaktoeButton(Pair(2, 1), board)
            TiktaktoeButton(Pair(2, 2), board)
        }
    }
}


@Composable
fun TiktaktoeButton(cords: Pair<Int, Int>, board: MutableState<Board>) {
    Button(onClick = {
        board.value = board.value.playPiece(cords.first, cords.second) ?: return@Button
    }
    ) {
        Text(board.value[cords.first, cords.second].toString())
    }
}
