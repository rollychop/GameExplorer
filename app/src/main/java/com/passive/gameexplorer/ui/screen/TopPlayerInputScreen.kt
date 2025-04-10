package com.passive.gameexplorer.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.passive.gameexplorer.ui.theme.GameExplorerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopPlayerInputScreen(
    onPlayerNameEntered: (String) -> Unit,
    onSubmit: () -> Unit
) {
    var playerName by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Enter Top Player Name",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Enter the name of your favorite player with the most kills",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(32.dp))

                BasicTextField(
                    value = playerName,
                    onValueChange = { playerName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    decorationBox = { innerText ->
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .border(
                                    2.dp,
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.shapes.small
                                )
                                .padding(8.dp)

                        ) {
                            if (playerName.text.isEmpty()) {
                                Text(
                                    "Enter your name",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = LocalContentColor.current.copy(.7f)
                                )
                            }
                            innerText()

                        }
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        onPlayerNameEntered(playerName.text)
                        onSubmit()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = playerName.text.isNotEmpty()
                ) {
                    Text("Submit")
                }
            }
        }
    )
}

@Preview
@Composable
private fun TopPlayerInputScreenPreview() {
    GameExplorerTheme {
        TopPlayerInputScreen(
            onPlayerNameEntered = {},
            onSubmit = {}
        )
    }
}
