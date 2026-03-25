package edu.moravian.csci215.tic_tac_toe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import tictactoe.composeapp.generated.resources.Res
import tictactoe.composeapp.generated.resources.easy_ai_display
import tictactoe.composeapp.generated.resources.hard_ai_display
import tictactoe.composeapp.generated.resources.human_display
import tictactoe.composeapp.generated.resources.medium_ai_display
import tictactoe.composeapp.generated.resources.name_field_label
import tictactoe.composeapp.generated.resources.no_name_error
import tictactoe.composeapp.generated.resources.player_one
import tictactoe.composeapp.generated.resources.player_two
import tictactoe.composeapp.generated.resources.randomNames
import tictactoe.composeapp.generated.resources.start_game_button
import tictactoe.composeapp.generated.resources.subtitle
import tictactoe.composeapp.generated.resources.title
import tictactoe.composeapp.generated.resources.type_field

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
    val scrollState = rememberScrollState()
    val randomNames = stringArrayResource(Res.array.randomNames).toList()
    val noNameErrorMessage = stringResource(Res.string.no_name_error)
    var playerOneName by remember { mutableStateOf(randomNames.random()) }
    var playerTwoName by remember { mutableStateOf(randomNames.random()) }
    var playerOneType by remember { mutableStateOf(PlayerType.Human) }
    var playerTwoType by remember { mutableStateOf(PlayerType.Human) }

    fun startConfiguredGame() {
        val trimmedOne = playerOneName.trim()
        val trimmedTwo = playerTwoName.trim()

        if (trimmedOne.isBlank() || trimmedTwo.isBlank()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(noNameErrorMessage)
            }
            return
        }

        onStartGame(
            PlayerConfig(name = trimmedOne, type = playerOneType),
            PlayerConfig(name = trimmedTwo, type = playerTwoType),
        )
    }

    BoxWithConstraints(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
    ) {
        val landscape = maxWidth > maxHeight

        if (landscape) {
            Row(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.Top,
            ) {
                TitleCopy(
                    modifier =
                        Modifier
                            .weight(0.95f)
                            .fillMaxHeight(),
                )
                PlayerSetupContent(
                    playerOneName = playerOneName,
                    onPlayerOneNameChange = { playerOneName = it },
                    playerOneType = playerOneType,
                    onPlayerOneTypeChange = { playerOneType = it },
                    playerTwoName = playerTwoName,
                    onPlayerTwoNameChange = { playerTwoName = it },
                    playerTwoType = playerTwoType,
                    onPlayerTwoTypeChange = { playerTwoType = it },
                    onStartGame = ::startConfiguredGame,
                    modifier = Modifier.weight(1.1f),
                )
            }
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                TitleCopy()
                PlayerSetupContent(
                    playerOneName = playerOneName,
                    onPlayerOneNameChange = { playerOneName = it },
                    playerOneType = playerOneType,
                    onPlayerOneTypeChange = { playerOneType = it },
                    playerTwoName = playerTwoName,
                    onPlayerTwoNameChange = { playerTwoName = it },
                    playerTwoType = playerTwoType,
                    onPlayerTwoTypeChange = { playerTwoType = it },
                    onStartGame = ::startConfiguredGame,
                )
            }
        }
    }
}

@Composable
private fun TitleCopy(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                text = stringResource(Res.string.title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = stringResource(Res.string.subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = "Classic strategy. Clean rounds. Responsive play.",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun PlayerSetupContent(
    playerOneName: String,
    onPlayerOneNameChange: (String) -> Unit,
    playerOneType: PlayerType,
    onPlayerOneTypeChange: (PlayerType) -> Unit,
    playerTwoName: String,
    onPlayerTwoNameChange: (String) -> Unit,
    playerTwoType: PlayerType,
    onPlayerTwoTypeChange: (PlayerType) -> Unit,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.widthIn(max = 640.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        PlayerSetupCard(
            title = stringResource(Res.string.player_one),
            currentName = playerOneName,
            onNameChange = onPlayerOneNameChange,
            selectedType = playerOneType,
            onTypeSelected = onPlayerOneTypeChange,
        )

        PlayerSetupCard(
            title = stringResource(Res.string.player_two),
            currentName = playerTwoName,
            onNameChange = onPlayerTwoNameChange,
            selectedType = playerTwoType,
            onTypeSelected = onPlayerTwoTypeChange,
        )

        Button(
            onClick = onStartGame,
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
            modifier = Modifier.fillMaxWidth(),
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
