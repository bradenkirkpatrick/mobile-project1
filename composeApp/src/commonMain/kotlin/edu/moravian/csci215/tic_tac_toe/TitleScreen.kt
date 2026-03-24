package edu.moravian.csci215.tic_tac_toe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.moravian.csci215.tic_tac_toe.game.EasyAIPlayer
import edu.moravian.csci215.tic_tac_toe.game.HardAIPlayer
import edu.moravian.csci215.tic_tac_toe.game.HumanPlayer
import edu.moravian.csci215.tic_tac_toe.game.MediumAIPlayer
import edu.moravian.csci215.tic_tac_toe.game.Player
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import tictactoe.composeapp.generated.resources.Res
import tictactoe.composeapp.generated.resources.*



/**
 * Navigation route for the welcome screen.
 */
@Serializable
data object Title

/**
 * The supported player types for a round of tic-tac-toe.
 */
enum class PlayerType(val label: String, val display: StringResource) {
    Human("Human", Res.string.human_display),
    EasyAI("Easy AI", Res.string.easy_ai_display),
    MediumAI("Medium AI", Res.string.medium_ai_display),
    HardAI("Hard AI", Res.string.hard_ai_display),
    ;
    val ai get() = getAI()
    fun getAI(): Player = when (this) {
        Human -> HumanPlayer()
        EasyAI -> EasyAIPlayer()
        MediumAI -> MediumAIPlayer()
        HardAI -> HardAIPlayer()
    }
}

/**
 * The user-entered setup data for one player.
 */
@Serializable
data class PlayerConfig(
    val name: String,
    val type: PlayerType,
)

/**
 * Shows the welcome screen where both players are configured before the game starts.
 */
@Composable
fun TitleScreen(
    snackbarHostState: SnackbarHostState,
    onStartGame: (PlayerConfig, PlayerConfig) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val randomNames = stringArrayResource(Res.array.randomNames).toList()
    val noNameErrorMessage = stringResource(Res.string.no_name_error)
    var playerOneName by remember { mutableStateOf(randomNames.random()) }
    var playerTwoName by remember { mutableStateOf(randomNames.random()) }
    var playerOneType by remember { mutableStateOf(PlayerType.Human) }
    var playerTwoType by remember { mutableStateOf(PlayerType.Human) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = stringResource(Res.string.title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(Res.string.subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        PlayerSetupCard(
            title = stringResource(Res.string.player_one),
            currentName = playerOneName,
            onNameChange = { playerOneName = it },
            selectedType = playerOneType,
            onTypeSelected = { playerOneType = it },
        )

        PlayerSetupCard(
            title = stringResource(Res.string.player_two),
            currentName = playerTwoName,
            onNameChange = { playerTwoName = it },
            selectedType = playerTwoType,
            onTypeSelected = { playerTwoType = it },
        )

        Button(
            onClick = {
                val trimmedOne = playerOneName.trim()
                val trimmedTwo = playerTwoName.trim()

                if (trimmedOne.isBlank() || trimmedTwo.isBlank()) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(noNameErrorMessage)
                    }
                    return@Button
                }

                onStartGame(
                    PlayerConfig(name = trimmedOne, type = playerOneType),
                    PlayerConfig(name = trimmedTwo, type = playerTwoType),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(Res.string.start_game_button))
        }
    }
}

@Composable
private fun PlayerSetupCard(
    title: String,
    currentName: String,
    onNameChange: (String) -> Unit,
    selectedType: PlayerType,
    onTypeSelected: (PlayerType) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            PlayerTypeDropdown(
                selectedType = selectedType,
                onTypeSelected = onTypeSelected,
            )
            OutlinedTextField(
                value = currentName,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.name_field_label)) },
                singleLine = true,
            )
        }
    }
}

@Composable
private fun PlayerTypeDropdown(
    selectedType: PlayerType,
    onTypeSelected: (PlayerType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            Text(stringResource(Res.string.type_field) + stringResource(selectedType.display))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            PlayerType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(stringResource(type.display)) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    },
                )
            }
        }
    }
}
