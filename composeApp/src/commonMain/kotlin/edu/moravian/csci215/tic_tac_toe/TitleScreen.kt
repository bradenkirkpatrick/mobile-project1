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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
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
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

/**
 * Navigation route for the welcome screen.
 */
@Serializable
data object Title

/**
 * The supported player types for a round of tic-tac-toe.
 */
enum class PlayerType(val label: String) {
    Human("Human"),
    EasyAI("Easy AI"),
    MediumAI("Medium AI"),
    HardAI("Hard AI"),
}

/**
 * The user-entered setup data for one player.
 */
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
    val randomNames = remember {
        listOf("Harper", "Riley", "Kai", "Sage", "Avery", "Quinn", "Rowan", "Ellis")
    }

    var playerOneName by remember { mutableStateOf(randomNames.random()) }
    var playerTwoName by remember { mutableStateOf(randomNames.random()) }
    var playerOneType by remember { mutableStateOf(PlayerType.Human) }
    var playerTwoType by remember { mutableStateOf(PlayerType.Human) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "Welcome to Tic-Tac-Toe",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Set up both players before starting the round.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        PlayerSetupCard(
            title = "Player 1",
            currentName = playerOneName,
            onNameChange = { playerOneName = it },
            selectedType = playerOneType,
            onTypeSelected = { playerOneType = it },
        )

        PlayerSetupCard(
            title = "Player 2",
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
                        snackbarHostState.showSnackbar("Both players need a name before the game can start.")
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
            Text("Start Game")
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
            modifier = Modifier
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
                label = { Text("Name") },
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
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Player Type: ${selectedType.label}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            PlayerType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.label) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    },
                )
            }
        }
    }
}
