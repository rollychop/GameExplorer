package com.passive.gameexplorer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.passive.gameexplorer.data.GameIdGenerator
import com.passive.gameexplorer.model.CommentModel
import com.passive.gameexplorer.model.GameDetailModel
import com.passive.gameexplorer.ui.components.GameDetailItem
import com.passive.gameexplorer.ui.components.generateAvatarUrl
import com.passive.gameexplorer.ui.components.generatePlayerName
import com.passive.gameexplorer.ui.theme.GameExplorerTheme
import com.passive.gameexplorer.viewmodels.GameViewModel

@Composable
fun GameDetailScreen(
    gameId: String,
    gameType: String,
    gameViewModel: GameViewModel
) {
    // UI States
    val isLoading by gameViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by gameViewModel.errorMessage.collectAsStateWithLifecycle()
    val message by gameViewModel.message.collectAsStateWithLifecycle()
    val gameDetails by gameViewModel.gameDetails.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        gameViewModel.fetchGameDetails(gameType, gameId)
    }
    GameDetailScreenContent(
        gameModel = gameDetails,
        isLoading = isLoading,
        onUserRatingSubmit = { rating ->
            gameViewModel.rateGame(gameType, gameId, rating)
        },
        onUserCommentSubmit = {
            gameViewModel.commentOnGame(gameType, gameId, it)
        },
    )

    if (errorMessage.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme
                        .scrim.copy(.8f)
                )
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    )
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: $errorMessage",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                onClick = {
                    gameViewModel.clearError()
                },
                modifier = Modifier.align(Alignment.TopEnd),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Icon(
                    Icons.Default.Close, "close"
                )
            }
        }
    }
    if (message.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme
                        .scrim.copy(.8f)
                )
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    )
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                onClick = {
                    gameViewModel.clearError()
                },
                modifier = Modifier.align(Alignment.TopEnd),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Icon(
                    Icons.Default.Close, "close"
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreenContent(
    gameModel: GameDetailModel?,
    onUserRatingSubmit: (rating: Int) -> Unit = { _ -> },
    onUserCommentSubmit: (comment: String) -> Unit = { _ -> },
    isLoading: Boolean = false,
) {

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Game Details")
            })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            gameModel?.let { model ->
                var userRating by remember { mutableIntStateOf(model.userRating?.rating ?: 0) }
                var userComment by remember {
                    mutableStateOf(
                        TextFieldValue(
                            model.userComment?.comment ?: ""
                        )
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                        .imePadding()
                ) {
                    GameDetailItem(
                        gameModel = model.gameModel,
                        modifier = Modifier
                    )
                    Spacer(Modifier.height(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = "Rate this Game",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            RatingBar(
                                rating = userRating,
                                onRatingChanged = { userRating = it },
                                enabled = model.userRating == null
                            )

                            Button(
                                onClick = {
                                    onUserRatingSubmit(userRating)
                                },
                                enabled = model.userRating == null,
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Submit Rating")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Leave a Comment",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            OutlinedTextField(
                                value = userComment,
                                onValueChange = { userComment = it },
                                label = { Text("Your Comment") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                enabled = model.userComment == null,

                                )

                            Button(
                                onClick = {
                                    onUserCommentSubmit(userComment.text)
                                },
                                modifier = Modifier.padding(top = 8.dp),
                                enabled = model.userComment == null,
                            ) {
                                Text("Submit Comment")
                            }
                        }
                    }

                    Text(
                        "All Comments",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    if (model.comments.isEmpty()) {
                        Text(
                            "No comments yet.",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        model.comments.forEach {
                            CommentCard(comment = it)
                        }
                    }
                    if (model.ratings.isNotEmpty()) {
                        val avgRating =
                            remember(model) { model.ratings.map { it.rating }.average() }
                        Text(
                            "Average Rating: %.1f (${model.ratings.size} ratings)".format(avgRating),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }


}


@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    enabled: Boolean = true,
) {
    Row {
        for (i in 1..5) {
            IconButton(onClick = { if (enabled) onRatingChanged(i) }) {
                Icon(
                    imageVector = if (i <= rating) Icons.Default.Star
                    else Icons.Default.StarBorder,
                    contentDescription = "Rate $i star"
                )
            }
        }
    }
}

@Composable
fun CommentCard(comment: CommentModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                generateAvatarUrl(comment.deviceId),
                "",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFFF8F00), CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier) {
                Text(
                    text = generatePlayerName(comment.deviceId),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Text(
                    text = comment.comment.capitalize(LocaleList.current),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameDetailScreen() {
    GameExplorerTheme {
        GameDetailScreenContent(
            GameDetailModel(
                gameModel = GameIdGenerator.generateBgmiGameId(),
                comments = listOf(),
                ratings = listOf(),
                userRating = null,
                userComment = null
            )
        )
    }
}

