package com.passive.gameexplorer.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.passive.gameexplorer.model.GameModel
import com.passive.gameexplorer.model.UserProfile
import com.passive.gameexplorer.ui.components.GameItem
import com.passive.gameexplorer.viewmodels.GameViewModel

@Composable
fun GameListScreenRoot(
    viewModel: GameViewModel,
    onGameClick: (GameModel) -> Unit
) {

    val gameIds by viewModel.gameIds.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    GameListScreen(
        gameIds = gameIds,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onGameClicked = onGameClick,
        profile = profile
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(
    gameIds: List<GameModel>,
    isLoading: Boolean,
    errorMessage: String,
    onGameClicked: (GameModel) -> Unit = {},
    profile: UserProfile?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Game Explorer")
                },
                actions = {
                    if (profile != null) {
                        Row(
                            modifier = Modifier.padding(end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = profile.coins.toString(),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "coins",
                                tint = Color(0xFFF9A825)
                            )
                        }

                    }
                }
            )
        }
    ) { innerPadding ->

        if (isLoading) {
            // Show loading indicator
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage.isNotEmpty()) {
            // Show error message
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error: $errorMessage", color = Color.Red, fontSize = 20.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(gameIds) { gameIdDetails ->
                    GameItem(
                        gameModel = gameIdDetails,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable { onGameClicked(gameIdDetails) }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun GameListScreenPreview() {
    val dummyGameIds = listOf(
        GameModel(
            gameId = "BGMI1234",
            gameType = "BGMI",
            gameLevel = "Diamond 💎",
            gameGuns = listOf("M416 🪓", "AWM 🎯", "AKM 🔫"),
            gameOutfits = listOf("Urban Warrior 👕", "Tactical Gear 🎽"),
            gameVehicles = listOf("Dune Buggy 🚙", "Motorbike 🏍️"),
            rating = 4f,
            createdAt = 1616164168000,
        ),
        GameModel(
            gameId = "FF5678",
            gameType = "Free Fire",
            gameLevel = "Master 🏆",
            gameGuns = listOf("MP40 🕹️", "Groza 🔥", "AK 🔫"),
            gameOutfits = listOf("Street King 👑", "Camo Sniper 🎯"),
            gameVehicles = listOf("Buggy 🚗", "Monster Truck 🚚"),
            rating = 5f,
            createdAt = 1616164168000
        )
    )
    GameListScreen(
        gameIds = dummyGameIds,
        isLoading = false,
        errorMessage = "",
        profile = UserProfile(coins = 10)
    )
}
