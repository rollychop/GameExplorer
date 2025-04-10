package com.passive.gameexplorer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.passive.gameexplorer.model.GameModel

@Composable
fun GameItem(
    gameModel: GameModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0F7FA))
                .padding(16.dp)
        ) {
            Text(
                text = "Game ID: ${gameModel.gameId}",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF00796B)
            )
            Text(
                text = "Game Type: ${gameModel.gameType}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Text(
                text = "Level: ${gameModel.gameLevel}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF0288D1)
            )
        }
    }
}