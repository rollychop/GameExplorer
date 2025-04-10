package com.passive.gameexplorer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.passive.gameexplorer.model.GameModel
import java.util.Random
import kotlin.math.abs

@Composable
fun GameItem(
    gameModel: GameModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F8E9))
                .padding(12.dp)
        ) {
            AsyncImage(
                model = generateAvatarUrl(gameModel.gameId),
                contentDescription = "Game Profile",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = generatePlayerName(gameModel.gameId),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF2E7D32)
                )

                Text(
                    text = "${gameModel.gameType} | ${gameModel.gameLevel}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Text(
                    text = "★ %.1f  •  💬 ${gameModel.totalComments}".format(gameModel.rating),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF616161)
                )
            }
        }
    }
}

fun generatePlayerName(gameId: String): String {
    val adjectives = listOf("Silent", "Fierce", "Swift", "Shadow", "Mighty", "Crazy", "Blazing")
    val nouns = listOf("Sniper", "Warrior", "Hunter", "Ghost", "Reaper", "Ninja", "Beast")
    val seed = gameId.hashCode().toLong()
    val rand = Random(seed)
    return "${adjectives[rand.nextInt(adjectives.size)]}${nouns[rand.nextInt(nouns.size)]}#${
        abs(
            seed % 1000
        )
    }"
}

fun generateAvatarUrl(gameId: String): String {
    // You can choose other styles like "bottts", "pixel-art", "adventurer", etc.
    return "https://api.dicebear.com/7.x/croodles/png?seed=$gameId"
}


